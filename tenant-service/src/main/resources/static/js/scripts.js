$(document).ready(function() {
    console.log('Document ready'); // Ensure this logs

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
    window.submitUserForm = function() {
        console.log('Button clicked'); // Ensure this logs

        // Clear any previous error messages
        $('.error-message').remove();
        $('.has-error').removeClass('has-error');

        // Serialize form data
        var formData = $('#addUserForm').serialize();
        console.log('Form data:', formData); // Log form data

        // Send the AJAX request
        $.ajax({
            type: 'POST',
            url: $('#addUserForm').attr('action'),
            data: formData,
            dataType: 'json', // Ensure JSON response format
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            success: function(response) {
                console.log(response); // Log full response to inspect it
                console.log('AJAX success'); // Ensure this logs
                $('#error-messages').empty();

                if (response.status === 'success') {
                    $('#content').html('<p>' + response.message + '</p>');
                    loadContent('/users/html', '#content'); // Example to reload the list
                } else {
                    // Check for global error (e.g. password mismatch)
                    if (response.errors.global) {
                        $('#error-messages').append('<p class="error-message" style="color:red;">' + response.errors.global + '</p>');
                    }

                    // Handle validation errors for specific fields
                    $.each(response.errors, function(field, error) {
                        if (field !== 'global') {
                            var fieldElement = $('[name=' + field + ']');
                            fieldElement.addClass('has-error');
                            
                            // Insert error message next to the respective field
                            fieldElement.after('<span class="error-message" style="color:red;">' + error + '</span>');
                        }
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error('Error Status:', status); // Log status
                console.error('Error Message:', error); // Log error message
                console.error('Response Text:', xhr.responseText); // Log response from server

                // Try to parse the response if it's JSON
                try {
                    var response = JSON.parse(xhr.responseText);
                    console.log('Parsed response:', response);
                    
                    $('#error-messages').empty();

                    // Handle validation errors similar to the success callback
                    if (response.status === 'error') {
                        if (response.errors.global) {
                            $('#error-messages').append('<p class="error-message" style="color:red;">' + response.errors.global + '</p>');
                        }

                        $.each(response.errors, function(field, error) {
                            var fieldElement = $('[name=' + field + ']');
                            fieldElement.addClass('has-error');
                            fieldElement.after('<span class="error-message" style="color:red;">' + error + '</span>');
                        });
                    } else {
                        $('#error-messages').html('<p class="error-message">Failed to add user. Please try again.</p>');
                    }
                } catch (e) {
                    // In case the response isn't JSON, show a generic error message
                    $('#error-messages').html('<p class="error-message">Failed to add user. Please try again.</p>');
                }
            }
        });
    };

    // Clear error messages when user interacts with the form fields
    $(document).on('lostfocus', 'input', function() {
        $(this).removeClass('has-error');
        $(this).next('.error-message').remove();
    });
});
