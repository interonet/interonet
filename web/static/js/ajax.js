/**
 * Created by houlifei on 16/5/27.
 */
     function getCookie(name) {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }


    $.ajaxSetup({
      beforeSend: function(xhr, settings){

          xhr.setRequestHeader("X-CSRFToken", getCookie('csrftoken'));              //request头需要添加csrftoken,告诉服务器，我是个好yin哪，我是正常访问者
      }
    });
