package vn.zgome.game.streetknight.game.main;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.game.IEntityGame;

public class NVC extends IEntityGame {

	public int id = 3;
	public int delta = 0;
	// FrameAnimation ani;
	public int frame = 0;
	public boolean isFlip;
	public boolean isBattu = false;
	public int tickBatTu = 0;
	public int totalBattu = 10000;

	public int x, y, wi, hi,deltaConner;
	public int xHead, yHead;
	public int hiConner;
	public int wiLeftAT, wiRightAT;
	public float legL, legR;
	public int hiBlankDown;
	public static float sin, cos, sin1, cos1;

	
	
	public boolean isLoeSang;
	public int timeLoeSang;

	public boolean isNhapNhay;
	public int nhapnhay;
	public int tickHien;
	public int tickAn;
	public int stateNhapNhay;
	public int countNhapNhay;

	public void nhapnhay() {
		isNhapNhay = true;
		nhapnhay = 0;
		tickHien = 0;
		tickAn = 0;
		stateNhapNhay = 1;
		countNhapNhay = 1;
	}

	public void updateNhapNhay(int delayTime) {
		if (isNhapNhay) {
			if (stateNhapNhay == 1) {
				tickAn += delayTime;
				if (tickAn >= 200) {
					tickAn = 0;
					stateNhapNhay = 2;
					tickHien = 0;
					nhapnhay = 1;
					countNhapNhay++;
				}
			} else if (stateNhapNhay == 2) {
				tickHien += delayTime;
				if (tickHien >= 200) {
					tickHien = 0;
					stateNhapNhay = 1;
					tickAn = 0;
					nhapnhay = 0;
					countNhapNhay++;
				}
			}
			if (countNhapNhay == 5) {
				isNhapNhay = false;
			}
		}
	}

	public void battu(int totalBattu) {
		tickBatTu = 0;
		isBattu = true;
		this.totalBattu = totalBattu;
	}

	public void updateBattu(int delayTime) {
		if (isBattu) {
			if (tickBatTu >= totalBattu) {
				tickBatTu = 0;
				isBattu = false;
			} else {
				tickBatTu += delayTime;
			}
		}
	}

	public void reset() {
		isNhapNhay = false;
		isLoeSang = false;
		timeLoeSang = 0;
		frame = 0;
		isFlip = false;
		isBattu = false;
		tickBatTu = 0;
		id = 1;
		for (int i = 1; i < 3; i++) {
			if (game.menuScreen.shopUi.shopData.tab1Item[1].isEquipe) {
				id = 2;
			} else if (game.menuScreen.shopUi.shopData.tab1Item[2].isEquipe) {
				id = 3;
			}
		}
		if (delta > 0) {
			id = 3;
		}
		if (id == 1) {
			hiBlankDown = game.Y(15);
			x = game.X(280);
			wi = game.X(300);
			hi = game.Y(250);
			xHead = game.X(150);
			yHead = game.Y(125);
			wiLeftAT = game.X(50);
			wiRightAT = game.X(50);
			hiConner = game.Y(170);
			deltaConner = game.X(70);
			legL = 0.45f;
			legR = 0.6f;
			if (_screen.stage == 1) {
				y = game.Y(50);
			} else if (_screen.stage == 2) {
				y = game.Y(50);
			} else if (_screen.stage == 3) {
				y = game.Y(50);
			} else if (_screen.stage == 4) {
				y = game.Y(50);
			} else if (_screen.stage == 5) {
				y = game.Y(57);
			} else if (_screen.stage == 6) {
				y = game.Y(50);
			} else if (_screen.stage == 7) {
				y = game.Y(50);
			}
		}
		if (id == 2) {
			x = game.X(170);
			y = game.Y(-25);
			wi = game.X(550);
			hi = game.Y(400);
			xHead = game.X(225);
			yHead = game.Y(200);
			wiLeftAT = game.X(100);
			wiRightAT = game.X(100);
			hiConner = game.Y(300);
			legL = 0.45f;
			legR = 0.55f;
			deltaConner = game.X(132);
		}
		if (id == 3) {
			if (delta == 0) {
				x = game.X(170);
				y = game.Y(-25);
				wi = game.X(550);
				hi = game.Y(400);
				xHead = game.X(225);
				yHead = game.Y(200);
				wiLeftAT = game.X(110);
				wiRightAT = game.X(110);
				hiConner = game.Y(300);
				legL = 0.45f;
				legR = 0.55f;
				deltaConner = game.X(76);
			} else if (delta == 1) {
				x = game.X(130);
				y = game.Y(10);
				wi = game.X(600);
				hi = game.Y(400);
				xHead = game.X(300);
				yHead = game.Y(230);
				wiLeftAT = game.X(150);
				wiRightAT = game.X(150);
				hiConner = game.Y(300);
				legL = 0.45f;
				legR = 0.55f;
				deltaConner = game.X(130);
			} else if (delta == 2) {
				x = game.X(280);
				y = game.Y(43);
				wi = game.X(350);
				hi = game.Y(250);
				xHead = game.X(125);
				yHead = game.Y(170);
				wiLeftAT = game.X(30);
				wiRightAT = game.X(30);
				hiConner = game.Y(240);
				legL = 0.45f;
				legR = 0.55f;
				deltaConner = game.X(60);
			}
		}
		// ==========================================
		float tan = ((float) ((game.Y(490)) - (_screen.nvc.y + _screen.nvc.yHead)) / (float) ((game
				.X(838)) - (_screen.nvc.x + _screen.nvc.xHead)));
		float degree = (float) Math.atan(tan);
		sin = Math.abs((float) (Math.sin(degree)));
		cos = Math.abs((float) Math.cos(degree));

		float tan1 = ((float) (game.Y(490) - (_screen.nvc.y + _screen.nvc.yHead)) / (float) (-(game
				.X(0)) + (_screen.nvc.x + _screen.nvc.xHead)));
		float degree1 = (float) Math.atan(tan1);
		sin1 = Math.abs((float) (Math.sin(degree1)));
		cos1 = Math.abs((float) Math.cos(degree1));
	}

	public NVC(GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		// ani = new FrameAnimation();
	}

	public void right() {
		frame = 1;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		// ani.setInfo(0, 15, 500);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub

		if (isNhapNhay && nhapnhay == 0)
			return;
		if (id == 1) {
			if (currentFrame == 0) {
				if (isWaitQ) {
					game.batcher.draw(Asset.nvc_normal.frames[0].region,
							x, y,
							Asset.nvc_normal.frames[0].wi,
							Asset.nvc_normal.frames[0].hi);
				} else {
					if (isQLeft) {
						game.batcher.draw(Asset.nvc_normal.frames[9].region,
								x, y,
								Asset.nvc_normal.frames[9].wi,
								Asset.nvc_normal.frames[9].hi);
					} else {
						game.batcher.draw(Asset.nvc_normal.frames[2].region,
								x, y,
								Asset.nvc_normal.frames[2].wi,
								Asset.nvc_normal.frames[2].hi);
					}
				}
				return;
			}
		}

		if (id == 1) {
			if (isFlip) {
				drawFlip();
			} else {
				game.batcher.draw(Asset.nvc_normal.frames[frame].region,
						x, y,
						Asset.nvc_normal.frames[frame].wi,
						Asset.nvc_normal.frames[frame].hi);
			}
		}

		if (id == 2) {
			if (isFlip) {
				drawFlip();
			} else {
				if (!isLoeSang && Asset.nvc_dao.frames[frame + 14] != null) {
					game.batcher.draw(Asset.nvc_dao.frames[frame + 14].region,
							x+game.X(550/2-400/2), y+game.Y(400/2-250/2),
							Asset.nvc_dao.frames[frame + 14].wi,
							Asset.nvc_dao.frames[frame + 14].hi);
				} else
					game.batcher.draw(Asset.nvc_dao.frames[frame].region,
							x, y,
							Asset.nvc_dao.frames[frame].wi,
							Asset.nvc_dao.frames[frame].hi);
			}
		}

		if (id == 3) {
			if (delta == 0) {
				if (isFlip) {
					drawFlip();
				} else {
					if (!isLoeSang && Asset.nvc_dau.frames[frame + 14] != null) {
						game.batcher.draw(
								Asset.nvc_dau.frames[frame + 14].region,
								x+game.X(550/2-400/2), y+game.Y(400/2-250/2),
								Asset.nvc_dau.frames[frame + 14].wi,
								Asset.nvc_dau.frames[frame + 14].hi);
					} else
						game.batcher.draw(Asset.nvc_dau.frames[frame].region,
								x,
								y,
								Asset.nvc_dau.frames[frame].wi,
								Asset.nvc_dau.frames[frame].hi);
				}
			} else if (delta == 1) {
				if (isFlip) {
					drawFlip();
				} else {
					if (!isLoeSang && Asset.nvc_ai.frames[frame + 14] != null) {
						game.batcher.draw(
								Asset.nvc_ai.frames[frame + 14].region, x, y,
								Asset.nvc_ai.frames[frame + 14].wi,
								Asset.nvc_ai.frames[frame + 14].hi);
					} else
						game.batcher.draw(Asset.nvc_ai.frames[frame].region, x,
								y, Asset.nvc_ai.frames[frame].wi,
								Asset.nvc_ai.frames[frame].hi);
				}
			} else if (delta == 2) {
				if (isFlip) {
					drawFlip();
				} else {
					game.batcher.draw(Asset.nvc_sa.frames[frame].region, x, y,
							Asset.nvc_sa.frames[frame].wi,
							Asset.nvc_sa.frames[frame].hi);
				}
			}
		}
	}

	public void drawFlip() {
		if (id == 1)
			game.batcher.draw(
					Asset.nvc_normal.frames[frame].region.getTexture(),
					x, y, Asset.nvc_normal.frames[frame].wi,
					Asset.nvc_normal.frames[frame].hi,
					Asset.nvc_normal.frames[frame].region.getRegionX(),
					Asset.nvc_normal.frames[frame].region.getRegionY(),
					Asset.nvc_normal.frames[frame].region.getRegionWidth(),
					Asset.nvc_normal.frames[frame].region.getRegionHeight(),
					true, false);
		if (id == 2) {
			if (!isLoeSang && Asset.nvc_dao.frames[frame + 14].region != null) {
				game.batcher.draw(Asset.nvc_dao.frames[frame + 14].region
						.getTexture(), x+game.X(550/2-400/2), y+game.Y(400/2-250/2), Asset.nvc_dao.frames[frame + 14].wi,
						Asset.nvc_dao.frames[frame + 14].hi,
						Asset.nvc_dao.frames[frame + 14].region.getRegionX(),
						Asset.nvc_dao.frames[frame + 14].region.getRegionY(),
						Asset.nvc_dao.frames[frame + 14].region
								.getRegionWidth(),
						Asset.nvc_dao.frames[frame + 14].region
								.getRegionHeight(), true, false);
			} else
				game.batcher.draw(
						Asset.nvc_dao.frames[frame].region.getTexture(),
						x,y,
						Asset.nvc_dao.frames[frame].wi,
						Asset.nvc_dao.frames[frame].hi,
						Asset.nvc_dao.frames[frame].region.getRegionX(),
						Asset.nvc_dao.frames[frame].region.getRegionY(),
						Asset.nvc_dao.frames[frame].region.getRegionWidth(),
						Asset.nvc_dao.frames[frame].region.getRegionHeight(),
						true, false);

		}
		if (id == 3) {
			if (delta == 0) {
				if (!isLoeSang && Asset.nvc_dau.frames[frame + 14] != null) {
					game.batcher.draw(Asset.nvc_dau.frames[frame + 14].region
							.getTexture(), x+game.X(550/2-400/2), y+game.Y(400/2-250/2), Asset.nvc_dau.frames[frame + 14].wi,
							Asset.nvc_dau.frames[frame + 14].hi,
							Asset.nvc_dau.frames[frame + 14].region
									.getRegionX(),
							Asset.nvc_dau.frames[frame + 14].region
									.getRegionY(),
							Asset.nvc_dau.frames[frame + 14].region
									.getRegionWidth(),
							Asset.nvc_dau.frames[frame + 14].region
									.getRegionHeight(), true, false);
				} else
					game.batcher
							.draw(Asset.nvc_dau.frames[frame].region
									.getTexture(), x,y,
									Asset.nvc_dau.frames[frame].wi,
									Asset.nvc_dau.frames[frame].hi,
									Asset.nvc_dau.frames[frame].region
											.getRegionX(),
									Asset.nvc_dau.frames[frame].region
											.getRegionY(),
									Asset.nvc_dau.frames[frame].region
											.getRegionWidth(),
									Asset.nvc_dau.frames[frame].region
											.getRegionHeight(), true, false);
			}
			if (delta == 1) {
				if (!isLoeSang && Asset.nvc_ai.frames[frame + 14] != null) {
					game.batcher
							.draw(Asset.nvc_ai.frames[frame + 14].region
									.getTexture(), x, y,
									Asset.nvc_ai.frames[frame + 14].wi,
									Asset.nvc_ai.frames[frame + 14].hi,
									Asset.nvc_ai.frames[frame + 14].region
											.getRegionX(),
									Asset.nvc_ai.frames[frame + 14].region
											.getRegionY(),
									Asset.nvc_ai.frames[frame + 14].region
											.getRegionWidth(),
									Asset.nvc_ai.frames[frame + 14].region
											.getRegionHeight(), true, false);
				} else
					game.batcher
							.draw(Asset.nvc_ai.frames[frame].region
									.getTexture(), x, y,
									Asset.nvc_ai.frames[frame].wi,
									Asset.nvc_ai.frames[frame].hi,
									Asset.nvc_ai.frames[frame].region
											.getRegionX(),
									Asset.nvc_ai.frames[frame].region
											.getRegionY(),
									Asset.nvc_ai.frames[frame].region
											.getRegionWidth(),
									Asset.nvc_ai.frames[frame].region
											.getRegionHeight(), true, false);
			}
			if (delta == 2)
				game.batcher.draw(
						Asset.nvc_sa.frames[frame].region.getTexture(), x, y,
						Asset.nvc_sa.frames[frame].wi,
						Asset.nvc_sa.frames[frame].hi,
						Asset.nvc_sa.frames[frame].region.getRegionX(),
						Asset.nvc_sa.frames[frame].region.getRegionY(),
						Asset.nvc_sa.frames[frame].region.getRegionWidth(),
						Asset.nvc_sa.frames[frame].region.getRegionHeight(),
						true, false);
		}
	}

	int countTime;
	int currentFrame = 0;
	boolean isFlipCurrent;
	public boolean isUsed;
	public int tickQ;
	public boolean isQLeft;
	public boolean isWaitQ;
	public int countQ;

	@Override
	public void update(int delayTime) {
		// TODO Auto-generated method stub
		// ani.update(delayTime);
		updateBattu(delayTime);
		updateNhapNhay(delayTime);
		if (frame == currentFrame && isFlip == isFlipCurrent) {
			isUsed = true;
			countTime += delayTime;
			if (countTime >= 400) {
				isUsed = false;
			}
			timeLoeSang += delayTime;
			if (timeLoeSang >= 200) {
				isLoeSang = false;
			}
		} else {
			if(game.dataSave.sound && id == 1 && currentFrame !=0)
				Asset.achoo.play();
			currentFrame = frame;
			isFlipCurrent = isFlip;
			countTime = 0;
			isUsed = true;
			_screen.score.move++;

			if (id != 1 && !(id == 3 && delta == 2)) {
				if (currentFrame != 0)
					Asset.vu.play(game.dataSave.sound ? 1f : 0f);
			}

			isLoeSang = true;
			timeLoeSang = 0;
		}
		if (id == 1 && currentFrame == 0 && isUsed == false) {
			if (isWaitQ) {
				if (tickQ > 2000) {
					tickQ = 0;
					isWaitQ = false;
					countQ = 0;
				} else {
					tickQ += delayTime;
				}
			} else {
				if (tickQ >= 500) {
					tickQ = 0;
					isQLeft = !isQLeft;
					countQ++;
					if (countQ == 2)
						isWaitQ = true;
				} else {
					tickQ += delayTime;
				}
			}
		}
	}
}
