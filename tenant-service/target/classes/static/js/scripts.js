$(document).ready(function() {
    console.log('Document ready');

    // Function to load content via AJAX
    function loadContent(url, target) {
        $.get(url, function(data) {
            $(target).html(data);
        }).fail(function() {
            $(target).html('<p class="error-message">Failed to load content. Please try again.</p>');
        });
    }

    // Event delegation for AJAX content loading
    $(document).on('click', '.ajax-link', function(event) {
        event.preventDefault();
        var url = $(this).attr('href');
        loadContent(url, '#content');
    });

    // Handle form submission with AJAX
window.submitHtmlForm = function(formId) {
    console.log('Button clicked');

    // Clear any previous error messages
    $('#error-messages').empty();
    $('.error-message').html('');
    $('input').removeClass('has-error');

    var form = $('#' + formId);
    if (form.length === 0) {
        console.error('Form not found');
        return;
    }

    var formData = form.serialize();
    var actionUrl = form.attr('action');

    if ($('#ref_id', form).length > 0) {
        var refId = $('#ref_id', form).val();
        if (!refId) {
            console.error('Reference ID not found');
            return;
        }
        actionUrl += '/' + refId;
    }

    console.log('Form data:', formData);

    $.ajax({
        type: 'POST',
        url: actionUrl,
        data: formData,
        dataType: 'json',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function(response) {
            console.log(response);

            if (response.status === 'error') {
                handleFormErrors(response);
            } else if (response.status === 'success') {
                // Show feedback message in a dialog
                $('<div>' + response.message + '</div>').dialog({
                    title: 'Success',
                    modal: true,
                    buttons: {
                        Ok: function() {
								$(this).dialog('close');
							    if (response.loadnext) {
				                    loadContent(response.loadnext, '#content');
				                }
                        }
                    }
                });

            }
        },
        error: function(xhr, status, error) {
            console.error('Error Status:', status);
            console.error('Error Message:', error);
            console.error('Response Text:', xhr.responseText);

            try {
                var response = JSON.parse(xhr.responseText);
                handleFormErrors(response);
            } catch (e) {
                $('#error-messages').html('<p class="error-message">Failed to process form. Please try again.</p>');
            }
        }
    });
};


    function handleFormErrors(response) {
        if (response.message) {
            $('#error-messages').append('<p class="error-message" style="color:red;">' + response.message + '</p>');
        }

        $.each(response.errors, function(field, error) {
            var fieldElement = $('[name=' + field + ']');
            if (fieldElement.length > 0) {
                fieldElement.addClass('has-error');
                fieldElement.after('<span class="error-message" style="color:red;">' + error + '</span>');
            } else {
                $('#error-messages').append('<p class="error-message" style="color:red;">' + error + '</p>');
            }
        });
    }

    // Clear error messages when interacting with form fields
    $(document).on('focus', 'input', function() {
        $(this).removeClass('has-error');
        $(this).next('.error-message').remove();
    });

    // Handle status toggle

});


function status_toggle(){
    $(document).ready(function() {
        $('.status-toggle').click(function() {
            const refId = $(this).data('id');
            const url = $(this).data('url');
            const currentStatus = $(this).text().trim();
            const newStatus = currentStatus === "Enable" ? 1 : 2;

            console.log(refId);

            $.ajax({
                type: 'POST',
                url: url,
                data: JSON.stringify({ userId: refId, userStatus: newStatus }),
                contentType: 'application/json',
                success: function(response) {
                    if (response.status === 'success') {
                        if (response.active === 'Y') {
                            $(this).html("Disable");
                            $('#flag_' + refId).removeClass('inactive_red').addClass('active_green');
                        } else {
                            $(this).html("Enable");
                            $('#flag_' + refId).removeClass('active_green').addClass('inactive_red');
                        }

                        // Show feedback message in a dialog
                        $('<div>' + response.message + '</div>').dialog({
                            title: 'Success',
                            modal: true,
                            buttons: {
                                Ok: function() {
                                    $(this).dialog('close');
                                }
                            }
                        });
                    } else {
                        console.error('Error updating status:', response.message);
                    }
                }.bind(this),
                error: function(error) {
                    console.error("Error toggling status:", error);
                }
            });
        });
    });
}

