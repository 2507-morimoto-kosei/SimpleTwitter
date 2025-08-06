$(document).ready(function(){
	// id='fuga'が設定されたボタンをクリックしたらダイアログを表示する。
	$('form[action*="deleteMessage"]').on('submit', function(event) {
		if (!confirm("本当に削除しますか？")) {
			event.preventDefault();
		}

	});
});