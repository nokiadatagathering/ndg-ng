var Utils = function() {
    return {
        encryptCredentials: function(form) {encryptCredentials(form);}
    };
    
    function encryptCredentials(form) {
        form.currentTarget.password.value = $.md5( form.currentTarget.username.value + ":NDG:" +  form.currentTarget.password.value );
        return true;
    }
}();