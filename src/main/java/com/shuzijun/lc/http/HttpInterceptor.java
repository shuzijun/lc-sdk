package com.shuzijun.lc.http;

import com.shuzijun.lc.errors.LcException;

public interface HttpInterceptor {
     InterceptorResult preHandle(HttpRequest request) throws LcException;
     public void postHandle(HttpRequest request,HttpResponse response) throws LcException;

     public static class InterceptorResult {
          private boolean abort;

          private HttpResponse response;

          private InterceptorResult(boolean abort,HttpResponse response){
               this.abort =abort;
               this.response = response;
          }

          public static InterceptorResult abort(HttpResponse response){
               return new InterceptorResult(true,response);
          }

          public static InterceptorResult continueWith(){
               return new InterceptorResult(false,null);
          }

          public boolean isAbort() {
               return abort;
          }

          public HttpResponse respond(){
               return response;
          }
     }
}


