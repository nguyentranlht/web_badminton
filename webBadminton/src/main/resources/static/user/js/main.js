(function ($) {
    "use strict";
    
    // Dropdown on mouse hover
    $(document).ready(function () {
        function toggleNavbarMethod() {
            if ($(window).width() > 992) {
                $('.navbar .dropdown').on('mouseover', function () {
                    $('.dropdown-toggle', this).trigger('click');
                }).on('mouseout', function () {
                    $('.dropdown-toggle', this).trigger('click').blur();
                });
            } else {
                $('.navbar .dropdown').off('mouseover').off('mouseout');
            }
        }
        toggleNavbarMethod();
        $(window).resize(toggleNavbarMethod);
    });
    
    
    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Date and time picker
    $('.date').datetimepicker({
        format: 'L'
    });
    $('.time').datetimepicker({
        format: 'LT'
    });


    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1500,
        margin: 30,
        dots: true,
        loop: true,
        center: true,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:1
            },
            768:{
                items:2
            },
            992:{
                items:3
            }
        }
    });


    
})(jQuery);
function sendMessage() {
    const input = document.querySelector('.chat-input');
    const message = input.value.trim();

    if (message !== '') {
        // Display user message immediately
        displayMessage(message, 'user');
        scrollChat();
        // Save message to session
        saveMessageToSession({text: message, sender: 'user'});

        // Send the message to Rasa
        fetch('http://localhost:5005/webhooks/rest/webhook', {  // Update the URL to your Rasa server's endpoint
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                message: message,
                sender: 'user',
            })
        })
        .then(response => response.json())
        .then(data => {
            // Display Rasa's response
            if (data && data.length > 0) {
                data.forEach((message) => {
                    displayMessage(message.text, 'bot');
                    scrollChat();
                    saveMessageToSession({text: message.text, sender: 'bot'});
                });
            }
        })
        .catch(error => console.error('Error talking to Rasa:', error));

        // Clear input field
        input.value = '';
    }
}

function displayMessage(message, sender) {
    const messageContainer = document.querySelector('.messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = 'd-flex flex-row p-3';  // Sets up flex row

    if (sender === 'bot') {
        // Creating and setting up the bot avatar image
        const botAvatar = document.createElement('img');
        botAvatar.src = "https://img.icons8.com/color/48/000000/circled-user-female-skin-type-7.png";
        botAvatar.width = 30;
        botAvatar.height = 30;
        botAvatar.className = 'ml-2';  // Add some margin to the right of the avatar

        // Creating the text container for the message
        const textDiv = document.createElement('div');
        textDiv.className = 'chat p-3';  // Assigns padding and potential other styles
        textDiv.textContent = message;

        // Appending the avatar and text div to the messageDiv
        messageDiv.appendChild(botAvatar);
        messageDiv.appendChild(textDiv);
    } else {
         const userTextDiv = document.createElement('div');
            userTextDiv.className = 'bg-white mr-2 p-3 flex-grow-1';  // Background white, margin-right for spacing
            const userTextSpan = document.createElement('span');
            userTextSpan.className = 'text-muted';  // Text styling
            userTextSpan.textContent = message;

            // Append the span to the div
            userTextDiv.appendChild(userTextSpan);

            // Creating and setting up the user avatar image
            const userAvatar = document.createElement('img');
            userAvatar.src = "https://img.icons8.com/color/48/000000/circled-user-male-skin-type-7.png";
            userAvatar.width = 30;
            userAvatar.height = 30;
            userAvatar.className = 'align-self-center';  // Align the avatar vertically in the middle

            messageDiv.style.justifyContent = 'flex-end';
            // Append elements in the correct order for the user
            messageDiv.appendChild(userTextDiv);
            messageDiv.appendChild(userAvatar);
    }

    // Append the fully constructed messageDiv to the container and scroll into view
    messageContainer.appendChild(messageDiv);// Auto-scroll to the latest message
}

function saveMessageToSession(messageObj) {
    let messages = JSON.parse(sessionStorage.getItem('chatMessages')) || [];
    messages.push(messageObj);
    sessionStorage.setItem('chatMessages', JSON.stringify(messages));
}

function loadMessagesFromSession() {
    let messages = JSON.parse(sessionStorage.getItem('chatMessages')) || [];
    messages.forEach(message => {
        displayMessage(message.text, message.sender);
    });
}

// Load messages when the chat box loads
document.addEventListener('DOMContentLoaded', loadMessagesFromSession);

function scrollChat(){
    const messageContainer = document.querySelector('.messages');
    messageContainer.scrollTop = messageContainer.scrollHeight;
}

function toggleChat() {
    var chatWrapper = document.getElementById('chatWrapper');
    if (chatWrapper.classList.contains('chat-closed')) {
            chatWrapper.classList.remove('chat-closed');
            chatWrapper.classList.add('chat-open');
        } else {
            chatWrapper.classList.remove('chat-open');
            chatWrapper.classList.add('chat-closed');
        }
}

function closeChat(event) {
    event.stopPropagation(); // Stop the event from bubbling up to parent elements
    const chatContainer = document.querySelector('#chatWrapper');
    if (chatContainer.style.display === 'none') {
        chatContainer.style.display = 'block'; // Shows the chat container
    } else {
        chatContainer.style.display = 'none'; // Hides the chat container
    }
}

