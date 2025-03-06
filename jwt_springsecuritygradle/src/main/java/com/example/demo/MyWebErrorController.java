package com.example.demo;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class MyWebErrorController implements ErrorController {//오버라이딩할 메소드 없다.
      @RequestMapping("/error")
      String handleError(HttpServletRequest request) throws Exception {
               Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
               if(status.toString().equals("403")) {
            	   return "접근불가(로그인이전이거나 해당페이지롤이 아닙니다)";
               }
               else if(status.toString().equals("404")) {
            	   return "요청처리할 수 없습니다";
               }
         return "기타오류";
      }
}
