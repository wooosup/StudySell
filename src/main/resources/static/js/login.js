
window.onload = function() {
    var urlParams = new URLSearchParams(window.location.search);
    var error = urlParams.get('error');

    if(error == 'true') {
    alert('올바르지 않은 접근입니다.');
    }
};
function confirmLogout() {
    var logoutConfirm = confirm("확인을 누르면 로그아웃이 완료됩니다.");
    if (logoutConfirm) {
    // 로그아웃 처리를 위한 서버로의 요청을 여기서 실행
    window.location.href = '/user/logout';
    }
}