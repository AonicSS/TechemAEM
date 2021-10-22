$(document).ready(function(){
    $("#csv_data").change(function(){
        $("#fileSelection").text($("#csv_data").val().replace("C:\\fakepath\\", ""));
    });

    $("#btnSubmit").click(function(e){
        e.preventDefault();
        if($("#csv_data").val().length > 0) {
            var d = new FormData($('#fileUploadForm')[0]);

            $("#btnSubmit").prop("disabled", true);
            var t = generateToast("Please wait, import in progress!", 1500, "info").show();

            $.ajax({
                type: "POST",
                enctype: "multipart/form-data",
                url: "/bin/techem/bulkimport",
                data: d,
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    showDialog('Success', data["msg"]);
                    $("#btnSubmit").prop("disabled", false);
                    t.hide();
                },
                error: function (data) {
                    showDialog('Failure', data["responseJSON"]["msg"]);
                    $("#btnSubmit").prop("disabled", false);
                    t.hide();
                }
            });

        }else {
            generateToast("Please choose a file!", 1500, "error").show();
        }
    });

    function showDialog(h, m){
        var dialog = new Coral.Dialog().set({
            id: "bulk_import_result",
            header: {
                innerHTML: '<b>' + h + '</b>'
            },
            content: {
                innerHTML: m
            },
            footer: {
                innerHTML: '<button is="coral-button" variant="primary" coral-close>Ok</button>'
            }
        });

        dialog.show();
    }

    function generateToast(msg, time, variant) {
        var toast = new Coral.Toast().set({
            content: {
              textContent: msg
            },
            autoDismiss: time,
            variant: variant
        });
        return toast;
    }
});