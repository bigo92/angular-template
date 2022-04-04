<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
    // register the interceptor as a service
    var myHttpInterceptor = function ($q) {
        return {
            // optional method
            'request': function (config) {
                // do something on success
                return config;
            },

            // optional method
            'requestError': function (rejection) {
                // do something on error
                if (canRecover(rejection)) {
                    return responseOrNewPromise
                }
                return $q.reject(rejection);
            },



            // optional method
            'response': function (response) {
                // do something on success
                return response;
            },

            // optional method
            'responseError': function (rejection) {
                // truong hop call ajax bị hết hạn token thì chuyển sang trang login
                if (rejection.status === 401) {
                    var urlback = location.pathname;
                    if (!urlback) { urlback = '/'; }
                    location.href = $('#contextPath').val() + '/login?urlback=' + urlback;
                }
                // do something on error
                if (canRecover(rejection)) {
                    return responseOrNewPromise
                }
                return $q.reject(rejection);
            }
        };
    };
</script>