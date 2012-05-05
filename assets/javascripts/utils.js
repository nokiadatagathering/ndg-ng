var Utils = function() {
    return {
        redirectIfUnauthorized : function(jqXHR, textStatus, errorThrown) { return redirectIfUnauthorized(jqXHR, textStatus, errorThrown)}
    };

    function redirectIfUnauthorized(jqXHR, textStatus, errorThrown) {
        if(errorThrown === "Unauthorized") {
            window.location = "login";
            return true;
        }
    return false;
    }
}();
