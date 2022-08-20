/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApplication.controller;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.TabStop;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.PageSize;
import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.request.ERechargeReq;
import fpt.aptech.ParkingApplication.domain.request.BookingReq;
import fpt.aptech.ParkingApplication.domain.request.EBookingReq;
import fpt.aptech.ParkingApplication.domain.request.NewBookingReq;
import fpt.aptech.ParkingApplication.domain.response.BookingDetailRes;
import fpt.aptech.ParkingApplication.domain.response.EPaymentRes;
import fpt.aptech.ParkingApplication.domain.response.LoginRes;
import fpt.aptech.ParkingApplication.domain.response.PageTransactionRes;
import fpt.aptech.ParkingApplication.domain.response.ParkingRes;

import fpt.aptech.ParkingApplication.domain.response.ProfileRes;
import fpt.aptech.ParkingApplication.domain.response.TransactionRes;
import fpt.aptech.ParkingApplication.utils.ModelMapperUtil;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import javafx.scene.text.TextAlignment;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author PCvinhvizg
 */
@Controller
public class TransactionController {

    @Value("${spring.data.rest.base-path}")
    private String PATH_API;

    @Autowired
    private ModelMapperUtil mapperUtil;
    @Autowired
    private RestTemplateConfiguration restTemplate;
    
    @RequestMapping(value = "/e-payment-detail", method = RequestMethod.GET)
    public String ePaymentDetail(@RequestParam Map<String, String> allMap, Model model, HttpSession session) {
        try {
            //check requestid - Momo: requestId , Zalopay: apptransid
            String requestid = allMap.get("requestId");
            if (requestid == null) {
                requestid = allMap.get("apptransid");
            }
            EPaymentRes orderRes = (EPaymentRes) session.getAttribute(requestid);
            HttpEntity transRequest = restTemplate.setRequest(orderRes.getTransactionReq());
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "checkStatus", HttpMethod.POST, transRequest, EPaymentRes.class);
            EPaymentRes orderResponse = (EPaymentRes) response.getBody();
            if (orderResponse.getReturnCode().equals(0) && orderRes.getTransactionReq().getStype().equals("e-Booking")) {
                // create booking
                BookingReq bookingReq = (BookingReq) session.getAttribute(orderResponse.getTransNo() + orderRes.getTransactionReq().getParkingname());
                NewBookingReq newBookingReq = new NewBookingReq(
                    orderRes.getTransactionReq().getUsername(),
                    bookingReq.getStarttime(),
                    bookingReq.getTimenumber(),
                    bookingReq.getLocationcode(),
                    bookingReq.getCarname(),
                    bookingReq.getLisenceplates(),
                    bookingReq.getParkingname(),
                    false
                );
                HttpEntity newbookingRequest = restTemplate.setRequest(newBookingReq);
                ResponseEntity<?> newbookingResponse = restTemplate.excuteRequest(PATH_API + "booking", HttpMethod.POST, newbookingRequest, Integer.class);
                HttpStatus status = newbookingResponse.getStatusCode();
                if (status.equals(HttpStatus.OK)) {
                    Integer id = (Integer) newbookingResponse.getBody();
                    return "redirect:/booking-details?id=" + id;
                    //return "redirect:/profile";
                }
            } else {
                model.addAttribute("transactionReq", orderRes.getTransactionReq());
                return "redirect:/profile";
            }
            return "redirect:/profile";
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/e-payment", method = RequestMethod.GET)
    public String ePayment(Model model, HttpSession session) {
        try {
            HttpEntity request = restTemplate.setRequest(((LoginRes)session.getAttribute("account")).getToken());
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "get-blance", HttpMethod.GET, request, Double.class);
            Double balance = (Double) response.getBody();
            ((LoginRes)session.getAttribute("account")).setBalance(balance);
            LoginRes account = (LoginRes) session.getAttribute("account");
            model.addAttribute("balance", String.valueOf(account.getBalance()));
            model.addAttribute("rechargeReq", new ERechargeReq());
            return "user/e-payment";
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/e-payment", method = RequestMethod.POST)
    public String createEPayment(@Valid @ModelAttribute("rechargeReq") ERechargeReq rechargeReq,
        BindingResult bindingResult, HttpSession session, Model model) {
        try {
            LoginRes loginRes = (LoginRes) session.getAttribute("account");
            if (bindingResult.hasErrors()) {
                model.addAttribute("balance", String.valueOf(loginRes.getBalance()));
                model.addAttribute("rechargeReq", new ERechargeReq());
                return "user/e-payment";
            }

            HttpEntity request = restTemplate.setRequest(rechargeReq);
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "e-recharge", HttpMethod.POST, request, EPaymentRes.class);
            EPaymentRes orderRes = (EPaymentRes) response.getBody();
            orderRes.getTransactionReq().setUsername(loginRes.getUsername());
            session.setAttribute(orderRes.getTransNo(), orderRes);
            String url = orderRes.getPayUrl();
            return "redirect:" + url;
//            return null;
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String userTransactions(@RequestParam("page") int currentPage, Model model, HttpSession session) {
        try {
            if (1 > currentPage) {
                currentPage = 1;
            }
            LoginRes loginRes = (LoginRes) session.getAttribute("account");
            String token = loginRes.getToken();
            HttpEntity request = restTemplate.setRequest(token);

            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "user-transactions?page=" + (currentPage - 1) + "&size=10", HttpMethod.GET, request, PageTransactionRes.class);
            PageTransactionRes pageTransactionRes = (PageTransactionRes) response.getBody();
            List<TransactionRes> usertransactions = pageTransactionRes.getListTransaction();
            if (currentPage > pageTransactionRes.getTotalPages()) {
                currentPage = pageTransactionRes.getTotalPages();
            }
            model.addAttribute("current", currentPage);
            int[] nav = new int[pageTransactionRes.getTotalPages()];
            for (int i = 0; i <= (pageTransactionRes.getTotalPages() - 1); i++) {
                nav[i] = i + 1;
            }
            model.addAttribute("pageList", nav);
            model.addAttribute("transactionlist", usertransactions);
            return "user/history";
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/history/search", method = RequestMethod.GET)
    public String userTransactionsSearch(@RequestParam("from-date") String fromDate, @RequestParam("to-date") String toDate, Model model, HttpSession session) {
        try {
            if (fromDate.isEmpty() || toDate.isEmpty()) {
                return "redirect:/history?page=0";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fromLocalDate = LocalDate.parse(fromDate, formatter);
            LocalDate toLocalDate = LocalDate.parse(toDate, formatter);
            LoginRes loginRes = (LoginRes) session.getAttribute("account");
            String token = loginRes.getToken();
            HttpEntity request = restTemplate.setRequest(token);
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "user-transactions/search?from-date=" + fromLocalDate + "&to-date=" + toLocalDate + "&page=0&size=100", HttpMethod.GET, request, PageTransactionRes.class);
            PageTransactionRes pageTransactionRes = (PageTransactionRes) response.getBody();
            List<TransactionRes> usertransactions = pageTransactionRes.getListTransaction();
            int[] nav = new int[pageTransactionRes.getTotalPages()];
            for (int i = 0; i <= (pageTransactionRes.getTotalPages() - 1); i++) {
                nav[i] = i + 1;
            }
            model.addAttribute("current", pageTransactionRes.getCurrentPage());
            model.addAttribute("pageList", nav);
            model.addAttribute("transactionlist", usertransactions);
            return "user/history";
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/booking", method = RequestMethod.GET)
    public String createEBooking(@ModelAttribute("bookingReq") BookingReq bookingReq, @RequestParam("parkingname") String parkingname, Model model, HttpSession session) {
        try {
            HttpEntity request = restTemplate.setRequest();
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "parking/" + parkingname, HttpMethod.GET, request, ParkingRes.class);
            ParkingRes parkingRes = (ParkingRes) response.getBody();
            BookingReq b = bookingReq;
            model.addAttribute("bookingReq", b);
            b.setParkingname(parkingname);
            String code = "A";
            int step = 1;
            String[] arrCode = new String[parkingRes.getNop()];
            for (int i = 0; i < parkingRes.getNop(); i++) {
                if (i != 0 && (i % parkingRes.getColumnofrow() == 0)) {
                    int charValue = code.charAt(0);
                    code = String.valueOf((char) (charValue + 1));
                    step = 1;
                }
                arrCode[i] = (code + (step));
                step++;
            }
            //get select date time
            String[] optionDate = new String[31];
            LocalDateTime lcCdurrentDate = LocalDateTime.now();
            for (int i = 0; i < 31; i++) {
                optionDate[i] = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(lcCdurrentDate.plusMinutes(i));
            }
            model.addAttribute("optionDate", optionDate);
            //call list location code booked
            ResponseEntity<?> response1 = restTemplate.excuteRequest(PATH_API
                + "booking-location-code?parkingname=" + parkingname,
                HttpMethod.GET, request, String[].class);
            model.addAttribute("listCodeBooked", response1.getBody());
            model.addAttribute("arrCode", arrCode);
            model.addAttribute("columnOfRow", parkingRes.getColumnofrow());
            model.addAttribute("rentcost", parkingRes.getRentcost());
            model.addAttribute("address", parkingRes.getAddress());
            return "user/booking";
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    public String createEBooking(@Valid @ModelAttribute("bookingReq") BookingReq bookingReq,
            BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            HttpEntity request = restTemplate.setRequest();
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "parking/" + bookingReq.getParkingname(), HttpMethod.GET, request, ParkingRes.class);
            ParkingRes parkingRes = (ParkingRes) response.getBody();
            String code = "A";
            int step = 1;
            String[] arrCode = new String[parkingRes.getNop()];
            for (int i = 0; i < parkingRes.getNop(); i++) {
                if (i != 0 && (i % parkingRes.getColumnofrow() == 0)) {
                    int charValue = code.charAt(0);
                    code = String.valueOf((char) (charValue + 1));
                    step = 1;
                }
                arrCode[i] = (code + (step));
                step++;
            }
            //get select date time
            String[] optionDate = new String[31];
            LocalDateTime lcCdurrentDate = LocalDateTime.now();
            for (int i = 0; i < 31; i++) {
                optionDate[i] = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(lcCdurrentDate.plusMinutes(i));
            }
            model.addAttribute("optionDate", optionDate);
            //call list location code booked
            ResponseEntity<?> response1 = restTemplate.excuteRequest(PATH_API
                    + "booking-location-code?parkingname=" + bookingReq.getParkingname(),
                    HttpMethod.GET, request, String[].class);
            model.addAttribute("listCodeBooked", response1.getBody());
            model.addAttribute("arrCode", arrCode);
            model.addAttribute("columnOfRow", parkingRes.getColumnofrow());
            model.addAttribute("rentcost", parkingRes.getRentcost());
            model.addAttribute("address", parkingRes.getAddress());
            redirectAttributes.addFlashAttribute("bookingReq", bookingReq);
            return "user/booking";
        }
        try {
            if (bookingReq.getChannel().equals("Wallet")) {
                LoginRes loginRes = (LoginRes) session.getAttribute("account");
                NewBookingReq newBookingReq = new NewBookingReq(loginRes.getUsername(),
                    bookingReq.getStarttime(), bookingReq.getTimenumber(),
                    bookingReq.getLocationcode(), bookingReq.getCarname(),
                    bookingReq.getLisenceplates(), bookingReq.getParkingname(), true);
                HttpEntity request = restTemplate.setRequest(newBookingReq);
                ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "booking", HttpMethod.POST, request, String.class);
                if (response.getStatusCode().equals(HttpStatus.OK)) {
                    int id = Integer.valueOf(response.getBody().toString());
                    return "redirect:/booking-details?id=" + id;
                } else {
                    return "badrequest";
                }
            } else {
                LoginRes loginRes = (LoginRes) session.getAttribute("account");

                EBookingReq ebookingReq = new EBookingReq();
                ebookingReq.setChannel(bookingReq.getChannel());
                ebookingReq.setParkingname(bookingReq.getParkingname());
                ebookingReq.setTimenumber(bookingReq.getTimenumber());

                HttpEntity request = restTemplate.setRequest(ebookingReq);
                ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "e-booking", HttpMethod.POST, request, EPaymentRes.class);
                EPaymentRes orderRes = (EPaymentRes) response.getBody();
                orderRes.getTransactionReq().setUsername(loginRes.getUsername());

                session.setAttribute(orderRes.getTransNo() + orderRes.getTransactionReq().getParkingname(), bookingReq);
                session.setAttribute(orderRes.getTransNo(), orderRes);

                String url = orderRes.getPayUrl();
                return "redirect:" + url;
            }
//            return null;
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/a/list-transaction", method = RequestMethod.GET)
    public String adminAllTransaction(@RequestParam("page") int currentPage, Model model) {
        try {
            if (1 > currentPage) {
                currentPage = 1;
            }
            HttpEntity request = restTemplate.setRequest();
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "list-transaction?page="
                    + (currentPage - 1) + "&size=10", HttpMethod.GET, request, PageTransactionRes.class);
            PageTransactionRes pageTransactionRes = (PageTransactionRes) response.getBody();
            List<TransactionRes> usertransactions = pageTransactionRes.getListTransaction();
            if (currentPage > pageTransactionRes.getTotalPages()) {
                currentPage = pageTransactionRes.getTotalPages();
            }
            model.addAttribute("current", currentPage);
            int[] nav = new int[pageTransactionRes.getTotalPages()];
            for (int i = 0; i <= (pageTransactionRes.getTotalPages() - 1); i++) {
                nav[i] = i + 1;
            }
            model.addAttribute("pageList", nav);
            model.addAttribute("transactionlist", usertransactions);
            return "admin/recharge-history";
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/a/list-transaction/search", method = RequestMethod.GET)
    public String allTransactionsSearch(@RequestParam("from-date") String fromDate,
        @RequestParam("to-date") String toDate, Model model) {
        try {
            if (fromDate.isEmpty() || toDate.isEmpty()) {
                return "redirect:/a/list-transaction?page=0";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fromLocalDate = LocalDate.parse(fromDate, formatter);
            LocalDate toLocalDate = LocalDate.parse(toDate, formatter);
            HttpEntity request = restTemplate.setRequest();
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "all-transactions/search?from-date=" + fromLocalDate + "&to-date=" + toLocalDate + "&page=0&size=1000", HttpMethod.GET, request, PageTransactionRes.class);
            PageTransactionRes pageTransactionRes = (PageTransactionRes) response.getBody();
            List<TransactionRes> usertransactions = pageTransactionRes.getListTransaction();
            int[] nav = new int[pageTransactionRes.getTotalPages()];
            for (int i = 0; i <= (pageTransactionRes.getTotalPages() - 1); i++) {
                nav[i] = i + 1;
            }
            model.addAttribute("current", pageTransactionRes.getCurrentPage());
            model.addAttribute("pageList", nav);
            model.addAttribute("transactionlist", usertransactions);
            model.addAttribute("fromDate", fromDate);
            model.addAttribute("toDate", toDate);
            return "admin/recharge-history";
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/export/pdf", method = RequestMethod.GET)
    public String exportPDF(@RequestParam("id") String id, HttpServletResponse response,
        HttpSession session) throws IOException, DocumentException {
        //call api
        LoginRes loginRes = (LoginRes) session.getAttribute("account");
        String token = loginRes.getToken();
        HttpEntity request = RestTemplateConfiguration.setRequest(token);
        ResponseEntity<?> responseApi = RestTemplateConfiguration
            .excuteRequest(PATH_API + "booking-details?id=" + id, HttpMethod.GET, request, BookingDetailRes.class);
        BookingDetailRes bookingDetailRes = (BookingDetailRes) responseApi.getBody();

        response.setContentType("aplication/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = receipt.pdf";
        response.setHeader(headerKey, headerValue);

        //font
        Font blueFont = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new CMYKColor(255, 0, 0, 0));
        Font signatureFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.ITALIC, new CMYKColor(0, 255, 0, 0));
        Font yellowFont = FontFactory.getFont(FontFactory.COURIER, 14, Font.NORMAL, new CMYKColor(0, 0, 0, 100));

        Document document = new Document(com.itextpdf.text.PageSize.A5);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        //content
        //Create chapter and sections
        Paragraph chapterTitle = new Paragraph("RECEIPT PARKING", blueFont);
        chapterTitle.setAlignment(Element.ALIGN_CENTER);
        Chapter chapter1 = new Chapter(chapterTitle, 1);
        Paragraph chapterPname = new Paragraph("(" + bookingDetailRes.getParkingname() + ")");
        chapterPname.setAlignment(Element.ALIGN_CENTER);
        chapter1.add(chapterPname);
        Paragraph chapterNo = new Paragraph("No. " + id);
        chapter1.add(chapterNo);
        Paragraph chapterDate = new Paragraph("Date: " + bookingDetailRes.getStarttime().toString());
        chapter1.add(chapterDate);
        Paragraph chapterTimenumber = new Paragraph("Time number: " + bookingDetailRes.getTimenumber());
        chapter1.add(chapterTimenumber);
        Paragraph chapterCarname = new Paragraph("Car name: " + bookingDetailRes.getCarname());
        chapter1.add(chapterCarname);
        Paragraph chapterLp = new Paragraph("Lisence Plates: " + bookingDetailRes.getLisenceplates());
        chapter1.add(chapterLp);
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");

        Paragraph chapterAmount = new Paragraph("Total Amount: " + formatter.format(bookingDetailRes.getPrice()) + " VND");
        chapter1.add(chapterAmount);
        Paragraph chapterSignature = new Paragraph("Signature       ");
        chapterSignature.setAlignment(Element.ALIGN_RIGHT);
        chapter1.add(chapterSignature);
        Paragraph chapterSignature1 = new Paragraph("do van chien", signatureFont);
        chapterSignature1.setAlignment(Element.ALIGN_RIGHT);
        chapter1.add(chapterSignature1);
        chapter1.setNumberDepth(0);
        document.add(chapter1);
        document.close();
        return "redirect:/booking-details?id=" + id;
    }
}
