package vn.zgome.game.streetknight.database;

public class KungfuDBItem {
	// all variables
	private int itemId;
	private int scoreMax;
	private int kill;
	private int move;
	private int money;
	private int nextMap;
	private int stageHiscore;
	private int coutHPBonus;
	private boolean isBuy[] = new boolean[13];

	public KungfuDBItem(int itemId, int scoreMax, int kill, int move,
			int money, int nextMap, int stageHiscore, int coutHPBonus,
			boolean[] isBuy) {
		super();
		this.itemId = itemId;
		this.scoreMax = scoreMax;
		this.kill = kill;
		this.move = move;
		this.money = money;
		this.nextMap = nextMap;
		this.stageHiscore = stageHiscore;
		this.coutHPBonus = coutHPBonus;
		this.isBuy = isBuy;
	}

	public int getScoreMax() {
		return scoreMax;
	}

	public void setScoreMax(int scoreMax) {
		this.scoreMax = scoreMax;
	}

	public int getKill() {
		return kill;
	}

	public void setKill(int kill) {
		this.kill = kill;
	}

	public int getMove() {
		return move;
	}

	public void setMove(int move) {
		this.move = move;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getNextMap() {
		return nextMap;
	}

	public void setNextMap(int nextMap) {
		this.nextMap = nextMap;
	}

	public int getStageHiscore() {
		return stageHiscore;
	}

	public void setStageHiscore(int stageHiscore) {
		this.stageHiscore = stageHiscore;
	}

	public int getCoutHPBonus() {
		return coutHPBonus;
	}

	public void setCoutHPBonus(int coutHPBonus) {
		this.coutHPBonus = coutHPBonus;
	}

	public boolean[] getIsBuy() {
		return isBuy;
	}

	public void setIsBuy(boolean[] isBuy) {
		this.isBuy = isBuy;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
}
