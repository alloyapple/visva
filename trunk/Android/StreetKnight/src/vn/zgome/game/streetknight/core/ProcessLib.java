package vn.zgome.game.streetknight.core;

public class ProcessLib {

	GameOS game;

	public ProcessLib(GameOS game) {
		this.game = game;
	}

	public void onTransactionSuccess(final String message, final String amount,
			final String currentcy, final int erroCode) {
		if (game.android != null) {
			game.android.showToast("Thanh toán thành công");
			final int value = Integer.parseInt(amount);
			
			game.dataSave.money += value;
			game.dataSave.saveAll();
			game.menuScreen.shopUi.refresh();
		}
	}

	public void onTransactionError(final int errorCode, final String message) {
		if (game.android != null) {
			game.android.showToast("Thanh toán không thành công");
		}
	}

	public void onSmsAutoSuccess() {
		if (game.android != null) {
			game.android.showToast("Mua thành công");
			game.gameScreen.score.hp = 5;
			game.gameScreen.countDownUi.count();
			for(int i=0;i<100;i++)
			{
				if(game.gameScreen.enemyManager.enemys[i]!=null)
					game.gameScreen.enemyManager.enemys[i].isDie=true;
			}
		}
	}

	public void onSmsAutoError() {
		if (game.android != null) {
			game.android.showToast("Mua không thành công");
			game.gameScreen.loseUi.show();
		}
	}

	public void onSendSMSSuccess() {
		if (game.android != null) {
			game.android.showToast("Thanh toán thành công!");
			game.dataSave.money += 15000;
			game.dataSave.saveAll();
		}
	}

	public void onSendSMSFailure() {
		if (game.android != null) {
			game.android.showToast("Thanh toán không thành công!");
		}
	}
}
