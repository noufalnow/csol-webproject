$(document).ready(function () {
    console.log('Document ready');

    // Handle pagination with AJAX
    $(document).on('click', '.page', function (event) {
        event.preventDefault();

        var page = $(this).data('page');
        var entity = $(this).data('entity') || 'users';
        var container = $(this).data('target') || '#content';
        var sortField = $('.sort.active').data('field') || 'userId';
        var sortDir = $('.sort.active').data('dir') || 'asc';
        var search = encodeURIComponent($('input[name="search"]').val() || '');

        var url = '/' + entity + '/html?page=' + page + '&sortField=' + sortField + '&sortDir=' + sortDir + '&search=' + search;
        loadContent(url, container);
    });

    // Handle sort functionality with AJAX
    /*$(document).on('click', '.sort', function (event) {
        event.preventDefault();
        var sortField = $(this).data('field');
        sortEntities(sortField);
    });*/

    // Handle search input change with AJAX
    /*$(document).on('input', 'input[name="search"]', function () {
        searchEntities('/users/html', '#content', 'userId');
    });*/

    // Handle AJAX link clicks
    $(document).on('click', '.ajax-link', function (event) {
        event.preventDefault();
        var url = $(this).attr('href');
        loadContent(url, '#content');
    });

    // Form submission with AJAX
window.submitHtmlForm = function (formId) {
    console.log('Button clicked');

    $('#error-messages').empty(); // Clear error messages
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
        success: function (response) {
            if (response.status === 'error') {
                handleFormErrors(response);
            } else if (response.status === 'success') {
                $('<div>' + response.message + '</div>').dialog({
                    title: 'Success',
                    modal: true,
                    buttons: {
                        Ok: function () {
                            $(this).dialog('close');
                            if (response.loadnext) {
                                loadContent(response.loadnext, '#content');
                            }
                        }
                    }
                });
            }
        },
        error: function (xhr, status, error) {
            //console.error('Error Status:', status);
            //console.error('Error Message:', error);

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
    // Clear previous error messages
    $('#error-messages').empty(); // Clear error messages container
    $('.error-message').remove(); // Remove all existing error messages
    $('input, select').removeClass('has-error'); // Remove error classes

    // Display the general error message
    if (response.message) {
        $('#error-messages').append('<p class="error-message" style="color:red;">' + response.message + '</p>');
    }

    // Iterate through each error in the response
    $.each(response.errors, function (field, error) {
        // Normalize the field name to match the input names in the form
        var fieldIndex = field.match(/\d+/); // Extract the index (e.g., 0 or 1)
        var normalizedFieldName = field.replace(/([A-Z])/g, '_$1').toLowerCase().replace(/\[\d+\]/, '[]'); // Normalize to snake_case

        // Handle array-type fields
        if (fieldIndex) {
            // Construct the selector for the array fields
            var fieldElement = $('[name="' + normalizedFieldName + '"]').eq(fieldIndex[0]);

            if (fieldElement.length > 0) {
                // Highlight the field and display the error message
                fieldElement.addClass('has-error');
                fieldElement.after('<span class="error-message" style="color:red;">' + error + '</span>');
            } else {
                // If the field is not found, append the error message to the container
                $('#error-messages').append('<p class="error-message" style="color:red;">' + error + '</p>');
            }
        } else {
            // Handle regular fields (without array notation)
            var fieldElement = $('[name="' + field + '"]');

            if (fieldElement.length > 0) {
                // Highlight the field and display the error message
                fieldElement.addClass('has-error');
                fieldElement.after('<span class="error-message" style="color:red;">' + error + '</span>');
            } else {
                // If the field is not found, append the error message to the container
                $('#error-messages').append('<p class="error-message" style="color:red;">' + error + '</p>');
            }
        }
    });
}








    // Clear error messages when interacting with form fields
    $(document).on('focus', 'input', function () {
        $(this).removeClass('has-error');
        $(this).next('.error-message').remove();
    });

    // Status toggle function
    $(document).on('click', '.status-toggle', function () {
        var refId = $(this).data('id');
        var url = $(this).data('url');
        var currentStatus = $(this).text().trim();
        var newStatus = currentStatus === "Enable" ? 1 : 2;
        var element = $(this);

        $.ajax({
            type: 'POST',
            url: url,
            data: JSON.stringify({ userId: refId, userStatus: newStatus }),
            contentType: 'application/json',
            success: function (response) {
                if (response.status === 'success') {
                    element.html(response.active === 'Y' ? "Disable" : "Enable");
                    $('#flag_' + refId).toggleClass('active_green inactive_red');
                }
            },
            error: function (error) {
                console.error("Error toggling status:", error);
            }
        });
    });
});

// Search functionality
function searchEntities(listingUrl, targetContainer, defaultSortField) {
    var search = $('input[name="search"]').val();
    var sortField = $('.sort.active').data('field') || defaultSortField;
    var sortDir = $('.sort.active').data('dir') || 'asc';
    var page = $('.pagination .active').find('a').data('page') || 0;

    var url = listingUrl + '?page=' + page + '&sortField=' + sortField + '&sortDir=' + sortDir + '&search=' + encodeURIComponent(search);

    loadContent(url, targetContainer);  // Use loadContent to handle AJAX
}

function sortEntities(sortField) {
    var targetContainer = '#content'; // Container where the content will be loaded
    var defaultSortField = 'userId'; // Default sort field

    var sortDir = $('.sort[data-field="' + sortField + '"]').data('dir') === 'asc' ? 'desc' : 'asc';  // Toggle sort direction

    // Update the sorting state in the DOM
    $('.sort').removeClass('active').data('dir', 'asc'); // Reset all to 'asc'
    $('.sort[data-field="' + sortField + '"]').addClass('active').data('dir', sortDir);

    // Trigger the search with updated sort direction and field
    searchEntities(listingUrl, targetContainer, defaultSortField);
}


function loadContent(url, target) {
    $.ajax({
        url: encodeURI(url),
        method: 'GET',
        success: function (data) {
            $(target).html(data);
        },
        error: function () {
            $(target).html('<p class="error-message">Failed to load content. Please try again.</p>');
        }
    });
}
