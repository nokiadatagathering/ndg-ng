var Utils = function() {
    return {
        encryptCredentials: function(form) {encryptCredentials(form);},
        redirectIfUnauthorized : function(jqXHR, textStatus, errorThrown) { return redirectIfUnauthorized(jqXHR, textStatus, errorThrown)}
    };

    function encryptCredentials(form) {
        form.currentTarget.password.value = $.md5( form.currentTarget.username.value + ":NDG:" +  form.currentTarget.password.value );
        return true;
    }

    function redirectIfUnauthorized(jqXHR, textStatus, errorThrown) {
        if(errorThrown === "Unauthorized") {
            window.location = "login";
            return true;
        }
    return false;
    }
}();