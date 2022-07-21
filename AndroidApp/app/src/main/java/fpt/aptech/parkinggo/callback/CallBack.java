package fpt.aptech.parkinggo.callback;

import org.springframework.http.ResponseEntity;

public interface CallBack {
    public void callback(final ResponseEntity<?> response);
}
