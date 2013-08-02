package vn.zgome.game.streetknight.game.main;

import java.util.Random;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.game.IEntityGame;
import vn.zgome.game.streetknight.game.egypt.Bocap;
import vn.zgome.game.streetknight.game.egypt.Daogam;
import vn.zgome.game.streetknight.game.egypt.DauGauAC;
import vn.zgome.game.streetknight.game.egypt.DauGauAC2;
import vn.zgome.game.streetknight.game.egypt.Luoihai;
import vn.zgome.game.streetknight.game.halong.Bua;
import vn.zgome.game.streetknight.game.halong.DauGauHL;
import vn.zgome.game.streetknight.game.halong.DauGauHL2;
import vn.zgome.game.streetknight.game.halong.MoNeo;
import vn.zgome.game.streetknight.game.halong.OcSen;
import vn.zgome.game.streetknight.game.hoangsa.Chodao;
import vn.zgome.game.streetknight.game.hoangsa.Daodua;
import vn.zgome.game.streetknight.game.hoangsa.DauGauDao;
import vn.zgome.game.streetknight.game.hoangsa.DauGauDao2;
import vn.zgome.game.streetknight.game.hoangsa.Tenlua;
import vn.zgome.game.streetknight.game.japan.Dao;
import vn.zgome.game.streetknight.game.japan.DauGauJP1;
import vn.zgome.game.streetknight.game.japan.DauGauJP2;
import vn.zgome.game.streetknight.game.japan.Phitieu;
import vn.zgome.game.streetknight.game.japan.Ran;
import vn.zgome.game.streetknight.game.roma.DauGauRM1;
import vn.zgome.game.streetknight.game.roma.DauGauRM2;
import vn.zgome.game.streetknight.game.roma.Luoi3;
import vn.zgome.game.streetknight.game.roma.Nguadien;
import vn.zgome.game.streetknight.game.roma.Riu;
import vn.zgome.game.streetknight.game.saigon.DauGauSG1;
import vn.zgome.game.streetknight.game.saigon.DauGauSG2;
import vn.zgome.game.streetknight.game.saigon.Guoc;
import vn.zgome.game.streetknight.game.saigon.Maysaytoc;
import vn.zgome.game.streetknight.game.saigon.Meo;

public class EnemyManager extends IEntityGame {

	int countDownTime;
	public ILive[] enemys = new ILive[100];
	public float scale = 1.5f;
	public int[] timeSpawn = new int[7];

	int deltaConerLeft=5, deltaConerRight = 3;
	
	public void reset() {
		countDownTime = 1500;
		for (int i = 0; i < 100; i++) {
			enemys[i] = null;
		}
		firtCall[0] = false;
		firtCall[1] = false;
		firtCall[2] = false;
		firtCall[3] = false;

		firtCallYouAre[0] = false;
		firtCallYouAre[1] = false;
		firtCallYouAre[2] = false;
		firtCallYouAre[3] = false;
		current = 0;


		final int[][] initDelta = new int[][] { { 20, 25, 30, 33, 35 },
				{ 25, 30, 35, 38, 40 }, { 30, 35, 38, 40, 40 },
				{ 35, 38, 40, 40, 43 }, { 35, 40, 43, 45, 48 },
				{ 38, 40, 45, 48, 50 }, { 40, 45, 48, 50, 55 } };
		final int[][] initTimeSpawn = new int[][] {
				{ 1500, 1500, 1500, 1000, 500 }, { 1500, 1500, 800, 500, 500 }, 
				{ 1500, 1000, 800, 700, 500 }, { 1000, 900, 800, 500, 500 }, 
				{ 800, 800, 500, 500, 500 }, { 800, 500, 500, 500, 500 }, 
				{ 500, 500, 500, 500, 500 }};


		for (int i = 0; i < 5; i++) {
			timeSpawn[i] = initTimeSpawn[_screen.stage - 1][i];
			deltaX[i] = initDelta[_screen.stage - 1][i];
			deltaY[i] = initDelta[_screen.stage - 1][i];
		}
		isBigger3000 = false;
		timeCreateNewItem = 0;

		lastArchi = 0;
		currentArchi = 0;
	}

	public EnemyManager(GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		countDownTime = 1500;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 100; i++) {
			if (enemys[i] != null) {
				if (enemys[i].isDie == false) {
					enemys[i].draw();
				}
			}
		}
	}

	public void add(ILive live) {
		if (live.isBlood()) {
			if (_screen.nvc.isBattu == true)
				return;
		}
		for (int i = 0; i < 100; i++) {
			if (enemys[i] == null || enemys[i].isDie == true) {
				enemys[i] = null;
				enemys[i] = live;
				break;
			}
		}
	}

	public void processNewEnemy(int direct) {
		// Hà nội
		if (_screen.stage == 1) {

			if (direct == 1) {
				switch (new Random().nextInt(3)) {
				case 0:
					Caichay temp = new Caichay(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					// temp.setFPS(nor, attack);
					add(temp);
					break;
				case 1:
					Caikim temp1 = new Caikim(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp1);
					// temp1.setDelta(deltaX, deltaY);
					break;
				case 2:
					Chaibia chaibia = new Chaibia(game, screen);
					chaibia.set(1);
					chaibia.start();
					add(chaibia);
					chaibia.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					// chaibia.setDelta(deltaX, deltaY);
					// chaibia.setFPS(normal, attack);
					break;
				}
			} else if (direct == 2) {
				switch (new Random().nextInt(5)) {
				case 0:
					Caichay temp = new Caichay(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Caikim temp1 = new Caikim(game, screen);
					temp1.set(2);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					Chaibia chaibia = new Chaibia(game, screen);
					chaibia.set(1);
					chaibia.start();
					chaibia.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(chaibia);
					break;
				case 3:
					DauGauHN dauGauHN = new DauGauHN(game, screen);
					dauGauHN.set(2);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 4:
					DauGauHN2 dauGauHN2 = new DauGauHN2(game, screen);
					dauGauHN2.set(2);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 3) {
				ChoHN temp = new ChoHN(game, screen);
				temp.set(2);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				temp.setFPS(120, 600);
				add(temp);
			} else if (direct == 4) {
				switch (new Random().nextInt(3)) {
				case 0:
					Caichay temp = new Caichay(game, screen);
					temp.set(4);
					temp.start();
					add(temp);
					temp.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					break;
				case 1:
					Chaibia temp1 = new Chaibia(game, screen);
					temp1.set(4);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					Chaibia chaibia = new Chaibia(game, screen);
					chaibia.set(4);
					chaibia.start();
					add(chaibia);
					chaibia.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					break;
				}
			} else if (direct == 5) {
				switch (new Random().nextInt(5)) {
				case 0:
					Caichay temp = new Caichay(game, screen);
					temp.set(5);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Caikim temp1 = new Caikim(game, screen);
					temp1.set(5);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					Chaibia chaibia = new Chaibia(game, screen);
					chaibia.set(5);
					chaibia.start();
					add(chaibia);
					chaibia.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					break;
				case 3:
					DauGauHN dauGauHN = new DauGauHN(game, screen);
					dauGauHN.set(5);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 4:
					DauGauHN2 dauGauHN2 = new DauGauHN2(game, screen);
					dauGauHN2.set(5);
					dauGauHN2.start();
					add(dauGauHN2);
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					break;
				}
			} else if (direct == 6) {
				ChoHN temp = new ChoHN(game, screen);
				temp.set(5);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				temp.setFPS(120, 600);
				add(temp);
			}
		}
		// ===============================================================
		if (_screen.stage == 2) {
			if (direct == 1) {
				switch (new Random().nextInt(2)) {
				case 0:
					Bua temp = new Bua(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					MoNeo temp1 = new MoNeo(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 2) {
				switch (new Random().nextInt(4)) {
				case 0:
					Bua temp = new Bua(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					MoNeo temp1 = new MoNeo(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					DauGauHL dauGauHN = new DauGauHL(game, screen);
					dauGauHN.set(2);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 3:
					DauGauHL2 dauGauHN2 = new DauGauHL2(game, screen);
					dauGauHN2.set(2);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 3) {
				OcSen temp = new OcSen(game, screen);
				temp.set(2);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			} else if (direct == 4) {
				switch (new Random().nextInt(2)) {
				case 0:
					Bua temp = new Bua(game, screen);
					temp.set(4);
					temp.start();
					add(temp);
					temp.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					break;
				case 1:
					MoNeo temp1 = new MoNeo(game, screen);
					temp1.set(4);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 5) {
				switch (new Random().nextInt(4)) {
				case 0:
					Bua temp = new Bua(game, screen);
					temp.set(5);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					MoNeo temp1 = new MoNeo(game, screen);
					temp1.set(5);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					DauGauHL dauGauHN = new DauGauHL(game, screen);
					dauGauHN.set(5);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 3:
					DauGauHL2 dauGauHN2 = new DauGauHL2(game, screen);
					dauGauHN2.set(5);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 6) {
				OcSen temp = new OcSen(game, screen);
				temp.set(5);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			}
		}
		// =============================
		if (_screen.stage == 3) {
			if (direct == 1) {
				switch (new Random().nextInt(2)) {
				case 0:
					Guoc temp = new Guoc(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Maysaytoc temp1 = new Maysaytoc(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 2) {
				switch (new Random().nextInt(4)) {
				case 0:
					Guoc temp = new Guoc(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Maysaytoc temp1 = new Maysaytoc(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					DauGauSG1 dauGauHN = new DauGauSG1(game, screen);
					dauGauHN.set(2);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 3:
					DauGauSG2 dauGauHN2 = new DauGauSG2(game, screen);
					dauGauHN2.set(2);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 3) {
				Meo temp = new Meo(game, screen);
				temp.set(2);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			} else if (direct == 4) {
				switch (new Random().nextInt(2)) {
				case 0:
					Guoc temp = new Guoc(game, screen);
					temp.set(4);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Maysaytoc temp1 = new Maysaytoc(game, screen);
					temp1.set(4);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 5) {
				switch (new Random().nextInt(4)) {
				case 0:
					Guoc temp = new Guoc(game, screen);
					temp.set(5);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Maysaytoc temp1 = new Maysaytoc(game, screen);
					temp1.set(5);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					DauGauSG1 dauGauHN = new DauGauSG1(game, screen);
					dauGauHN.set(5);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 3:
					DauGauSG2 dauGauHN2 = new DauGauSG2(game, screen);
					dauGauHN2.set(5);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 6) {
				Meo temp = new Meo(game, screen);
				temp.set(5);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			}
		}
		// =====================================================
		else if (_screen.stage == 4) {
			if (direct == 1) {
				switch (new Random().nextInt(2)) {
				case 0:
					Dao temp = new Dao(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Phitieu temp1 = new Phitieu(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 2) {
				switch (new Random().nextInt(4)) {
				case 0:
					Dao temp = new Dao(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Phitieu temp1 = new Phitieu(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					DauGauJP1 dauGauHN = new DauGauJP1(game, screen);
					dauGauHN.set(2);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 3:
					DauGauJP2 dauGauHN2 = new DauGauJP2(game, screen);
					dauGauHN2.set(2);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 3) {
				Ran temp = new Ran(game, screen);
				temp.set(2);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			} else if (direct == 4) {
				switch (new Random().nextInt(2)) {
				case 0:
					Dao temp = new Dao(game, screen);
					temp.set(4);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Phitieu temp1 = new Phitieu(game, screen);
					temp1.set(4);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 5) {
				switch (new Random().nextInt(4)) {
				case 0:
					Dao temp = new Dao(game, screen);
					temp.set(5);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Phitieu temp1 = new Phitieu(game, screen);
					temp1.set(5);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					DauGauJP1 dauGauHN = new DauGauJP1(game, screen);
					dauGauHN.set(5);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 3:
					DauGauJP2 dauGauHN2 = new DauGauJP2(game, screen);
					dauGauHN2.set(5);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 6) {
				Ran temp = new Ran(game, screen);
				temp.set(5);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			}
		}
		// =====================================================
		// =====================================================
		else if (_screen.stage == 5) {
			if (direct == 1) {
				switch (new Random().nextInt(2)) {
				case 0:
					Riu temp = new Riu(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Luoi3 temp1 = new Luoi3(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 2) {
				switch (new Random().nextInt(4)) {
				case 0:
					Riu temp = new Riu(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Luoi3 temp1 = new Luoi3(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					DauGauRM1 dauGauHN = new DauGauRM1(game, screen);
					dauGauHN.set(2);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 3:
					DauGauRM2 dauGauHN2 = new DauGauRM2(game, screen);
					dauGauHN2.set(2);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 3) {
				Nguadien temp = new Nguadien(game, screen);
				temp.set(2);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			} else if (direct == 4) {
				switch (new Random().nextInt(2)) {
				case 0:
					Riu temp = new Riu(game, screen);
					temp.set(4);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Luoi3 temp1 = new Luoi3(game, screen);
					temp1.set(4);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 5) {
				switch (new Random().nextInt(4)) {
				case 0:
					Luoi3 temp = new Luoi3(game, screen);
					temp.set(5);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Luoi3 temp1 = new Luoi3(game, screen);
					temp1.set(5);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					DauGauRM1 dauGauHN = new DauGauRM1(game, screen);
					dauGauHN.set(5);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 3:
					DauGauRM2 dauGauHN2 = new DauGauRM2(game, screen);
					dauGauHN2.set(5);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 6) {
				Nguadien temp = new Nguadien(game, screen);
				temp.set(5);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			}
		}
		// =====================================================
		// =====================================================
		else if (_screen.stage == 6) {
			if (direct == 1) {
				switch (new Random().nextInt(2)) {
				case 0:
					Daogam temp = new Daogam(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Luoihai temp1 = new Luoihai(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 2) {
				switch (new Random().nextInt(4)) {
				case 0:
					Daogam temp = new Daogam(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Luoihai temp1 = new Luoihai(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					DauGauAC dauGauHN = new DauGauAC(game, screen);
					dauGauHN.set(2);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 3:
					DauGauAC2 dauGauHN2 = new DauGauAC2(game, screen);
					dauGauHN2.set(2);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 3) {
				Bocap temp = new Bocap(game, screen);
				temp.set(2);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			} else if (direct == 4) {
				switch (new Random().nextInt(2)) {
				case 0:
					Daogam temp = new Daogam(game, screen);
					temp.set(4);
					temp.start();
					add(temp);
					temp.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					break;
				case 1:
					Luoihai temp1 = new Luoihai(game, screen);
					temp1.set(4);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 5) {
				switch (new Random().nextInt(4)) {
				case 0:
					Daogam temp = new Daogam(game, screen);
					temp.set(5);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Luoihai temp1 = new Luoihai(game, screen);
					temp1.set(5);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				case 2:
					DauGauAC dauGauHN = new DauGauAC(game, screen);
					dauGauHN.set(5);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 3:
					DauGauAC2 dauGauHN2 = new DauGauAC2(game, screen);
					dauGauHN2.set(5);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 6) {
				Bocap temp = new Bocap(game, screen);
				temp.set(5);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			}
		}
		// =====================================================
		// =====================================================
		else if (_screen.stage == 7) {
			if (direct == 1) {
				switch (new Random().nextInt(2)) {
				case 0:
					Daodua temp = new Daodua(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Tenlua temp1 = new Tenlua(game, screen);
					temp1.set(1);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 2) {
				switch (new Random().nextInt(4)) {
				case 0:
					Daodua temp = new Daodua(game, screen);
					temp.set(1);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				// case 1:
				// Tenlua temp1 = new Tenlua(game, screen);
				// temp1.set(1);
				// temp1.start();
				// add(temp1);
				// break;
				case 1:
					DauGauDao dauGauHN = new DauGauDao(game, screen);
					dauGauHN.set(2);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 2:
					DauGauDao2 dauGauHN2 = new DauGauDao2(game, screen);
					dauGauHN2.set(2);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 3) {
				Chodao temp = new Chodao(game, screen);
				temp.set(2);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			} else if (direct == 4) {
				switch (new Random().nextInt(2)) {
				case 0:
					Daodua temp = new Daodua(game, screen);
					temp.set(4);
					temp.start();
					temp.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
					add(temp);
					break;
				case 1:
					Tenlua temp1 = new Tenlua(game, screen);
					temp1.set(4);
					temp1.start();
					temp1.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp1);
					break;
				}
			} else if (direct == 5) {
				switch (new Random().nextInt(4)) {
				case 0:
					Daodua temp = new Daodua(game, screen);
					temp.set(5);
					temp.start();
					temp.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(temp);
					break;
				// case 1:
				// Tenlua temp1 = new Tenlua(game, screen);
				// temp1.set(5);
				// temp1.start();
				// add(temp1);
				// break;
				case 1:
					DauGauDao dauGauHN = new DauGauDao(game, screen);
					dauGauHN.set(5);
					dauGauHN.start();
					dauGauHN.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN);
					break;
				case 2:
					DauGauDao2 dauGauHN2 = new DauGauDao2(game, screen);
					dauGauHN2.set(5);
					dauGauHN2.start();
					dauGauHN2.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					add(dauGauHN2);
					break;
				}
			} else if (direct == 6) {
				Chodao temp = new Chodao(game, screen);
				temp.set(5);
				temp.start();
				temp.setDelta(game.X(deltaX[current]), game.Y(deltaY[current]));
				add(temp);
			}
		}
		// =====================================================
	}

	Random rand = new Random();

	@Override
	public void update(int delayTime) {
		// TODO Auto-generated method stub

		if (countDownTime <= 0) {
			if (_screen.score.score >= 5000) {
				countDownTime = timeSpawn[4];
			} else if (_screen.score.score >= 3000) {
				countDownTime = timeSpawn[3];
			} else if (_screen.score.score >= 2000) {
				countDownTime = timeSpawn[2];
			} else if (_screen.score.score >= 1000) {
				countDownTime = timeSpawn[1];
			} else {
				countDownTime = timeSpawn[0];
			}

			switch (rand.nextInt(7)) {
			case 1:
				processNewEnemy(1);
				break;
			case 2:
				processNewEnemy(2);
				break;
			case 3:
				processNewEnemy(3);
				break;
			case 4:
				processNewEnemy(4);
				break;
			case 5:
				processNewEnemy(5);
				break;
			case 6:
				processNewEnemy(6);
				break;
			}
		} else {
			countDownTime -= delayTime;
		}

		for (int i = 0; i < 100; i++) {
			if (enemys[i] != null) {
				if (enemys[i].isDie == false) {
					enemys[i].update(delayTime);
				}
			}
		}
		// ================================================================
		if (_screen.score.score >= 5000) {
			if (firtCall[0] == false) {
				firtCall[0] = true;
				current = 4;
				for (int i = 0; i < 100; i++) {
					if (enemys[i] != null) {
						if (enemys[i].isDie == false) {
							enemys[i].setDelta(game.X(deltaX[current]),
									game.Y(deltaY[current]));
						}
					}
				}
			}
		} else if (_screen.score.score >= 3000) {
			if (firtCall[1] == false) {
				firtCall[1] = true;
				current = 3;
				for (int i = 0; i < 100; i++) {
					if (enemys[i] != null) {
						if (enemys[i].isDie == false) {
							enemys[i].setDelta(game.X(deltaX[current]),
									game.Y(deltaY[current]));
						}
					}
				}
			}
		} else if (_screen.score.score >= 2000) {
			if (firtCall[2] == false) {
				firtCall[2] = true;
				current = 2;
				for (int i = 0; i < 100; i++) {
					if (enemys[i] != null) {
						if (enemys[i].isDie == false) {
							enemys[i].setDelta(game.X(deltaX[current]),
									game.Y(deltaY[current]));
						}
					}
				}
			}
		} else if (_screen.score.score >= 1000) {
			if (firtCall[3] == false) {
				firtCall[3] = true;
				current = 1;
				for (int i = 0; i < 100; i++) {
					if (enemys[i] != null) {
						if (enemys[i].isDie == false) {
							enemys[i].setDelta(game.X(deltaX[current]),
									game.Y(deltaY[current]));
						}
					}
				}
			}
		}
		// =================================================================
		currentArchi = _screen.score.achivementPlaying(_screen.score.killRank);
		if (lastArchi != currentArchi) {
			switch (currentArchi) {
			case 1:
				_screen.rank.restart(Asset.novice);
				break;
			case 2:
				_screen.rank.restart(Asset.skillful);
				break;
			case 3:
				_screen.rank.restart(Asset.expert);
				break;
			case 4:
				_screen.rank.restart(Asset.unstoppable);
				break;
			case 5:
				_screen.rank.restart(Asset.master);
				break;
			}
		}
		lastArchi = currentArchi;
		// ===============================================
		if (!isBigger3000) {
			if (_screen.score.score >= 3000) {
				isBigger3000 = true;
				timeCreateNewItem = 4000;
			}
		} else {
			timeCreateNewItem -= delayTime;
			if (timeCreateNewItem <= 0) {
				if (((new Random().nextInt()) % 2) == 0) {
					Thuoc chaibia = new Thuoc(game, screen);
					int type = new Random().nextInt(6) + 1;
					chaibia.set(type);
					chaibia.start();
					add(chaibia);
					chaibia.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					if(type == 1)
						chaibia.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					else if(type==4)
						chaibia.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
				} else {
					Bom chaibia = new Bom(game, screen);
					int type = new Random().nextInt(6) + 1;
					chaibia.set(type);
					chaibia.start();
					add(chaibia);
					chaibia.setDelta(game.X(deltaX[current]),
							game.Y(deltaY[current]));
					if(type == 1)
						chaibia.setDelta(game.X(deltaX[current]+deltaConerRight),
							game.Y(deltaY[current]));
					else if(type==4)
						chaibia.setDelta(game.X(deltaX[current]+deltaConerLeft),
							game.Y(deltaY[current]));
				}
				timeCreateNewItem = 35000 + rand.nextInt(35) * 1000;
			}
		}
	}

	public void checkKill() {
		if (_screen.score.hp < 4) {
			_screen.rank.restart(Asset.novice);
		}
	}

	boolean[] firtCallYouAre = new boolean[5];
	boolean[] firtCall = new boolean[4];
	int[] deltaX = new int[5];
	int[] deltaY = new int[5];

	int current;
	int lastArchi;
	int currentArchi;
	boolean isBigger3000;
	int timeCreateNewItem;
}
