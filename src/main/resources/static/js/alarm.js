document.addEventListener('DOMContentLoaded', function() {
    console.log("DOMContentLoaded event fired");

    const notificationIcon = document.getElementById('notificationIcon');
    const notificationCount = document.getElementById('notificationCount');
    const notificationPopup = document.createElement('div');
    notificationPopup.id = 'notificationPopup';
    notificationPopup.style.display = 'none';
    notificationPopup.style.position = 'absolute';
    notificationPopup.style.backgroundColor = '#fff';
    notificationPopup.style.border = '1px solid #ccc';
    notificationPopup.style.padding = '10px';
    notificationPopup.style.zIndex = '1000';

    const noNotificationMessage = document.createElement('div');
    noNotificationMessage.id = 'noNotificationMessage';
    noNotificationMessage.textContent = '알림이 없습니다';
    notificationPopup.appendChild(noNotificationMessage);

    document.body.appendChild(notificationPopup);

    notificationIcon.addEventListener('click', function(event) {
        const rect = notificationIcon.getBoundingClientRect();
        notificationPopup.style.display = notificationPopup.style.display === 'none' ? 'block' : 'none';
        notificationPopup.style.left = rect.left + 'px';
        notificationPopup.style.top = rect.bottom + 'px';

        // If there are no notifications, show the noNotificationMessage
        if (notificationPopup.children.length === 1) {
            noNotificationMessage.style.display = 'block';
        } else {
            noNotificationMessage.style.display = 'none';
        }
    });

    function showNotification(message) {
        console.log('Displaying notification:', message);
        const notificationMessage = document.createElement('div');
        notificationMessage.textContent = message.senderName + '님에게 메시지가 왔습니다';
        notificationMessage.style.cursor = 'pointer';
        notificationMessage.addEventListener('click', function() {
            window.location.href = '/chat/' + message.chatRoomId;
        });

        // Remove noNotificationMessage if present
        noNotificationMessage.style.display = 'none';

        notificationPopup.appendChild(notificationMessage);

        const toast = document.createElement('div');
        toast.className = 'toast';
        toast.innerText = '새로운 메시지가 도착했습니다: ' + message.message;
        document.body.appendChild(toast);

        setTimeout(function() {
            toast.remove();
        }, 3000);
    }

    // WebSocket connection
    console.log("Setting up WebSocket connection");
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/sub/notifications', function(notification) {
            console.log('Notification received: ', notification.body);
            try {
                const messageDto = JSON.parse(notification.body);
                console.log('Parsed message DTO: ', messageDto);
                notificationCount.textContent = parseInt(notificationCount.textContent || '0') + 1;
                showNotification(messageDto);
            } catch (e) {
                console.error('Failed to parse notification body:', e);
            }
        });
    }, function(error) {
        console.error('WebSocket connection error: ', error);
    });
});
