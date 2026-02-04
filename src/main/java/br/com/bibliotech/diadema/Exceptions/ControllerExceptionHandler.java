package br.com.bibliotech.diadema.Exceptions;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<?> handleConflict(ConflictException ex){
            return ResponseEntity.status(409).body(ex.getMessage());
        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<?> handleNotFound(NotFoundException ex){
            return ResponseEntity.status(404).body(ex.getMessage());
        }

        @ExceptionHandler(NullException.class)
        public ResponseEntity<?> handleNull(NullException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        }

        @ExceptionHandler(SintaxeException.class)
        public ResponseEntity<?> handleSintaxe(SintaxeException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<?> handleBadRequest(BadRequestException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        }

}
