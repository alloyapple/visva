package vn.zgome.game.streetknight.menu.shop;

import vn.zgome.game.streetknight.core.GameOS;

public class ShopData 
{
	public int tab;
	public int item;
	public InfoItem[] tab1Item;
	public InfoItem[] tab2Item;
	public InfoItem[] tab3Item;
	
	public GameOS game;
		
	public void getDataSave()
	{
		tab1Item[0].isBuy = game.dataSave.isBuy[0];
		tab1Item[0].isEquipe = game.dataSave.isEquip[0];
		tab1Item[1].isBuy = game.dataSave.isBuy[1];
		tab1Item[1].isEquipe = game.dataSave.isEquip[1];
		tab1Item[2].isBuy = game.dataSave.isBuy[2];
		tab1Item[2].isEquipe = game.dataSave.isEquip[2];
		tab1Item[3].isBuy = game.dataSave.isBuy[3];
		tab1Item[3].isEquipe = game.dataSave.isEquip[3];
		tab1Item[4].isBuy = game.dataSave.isBuy[4];
		tab1Item[4].isEquipe = game.dataSave.isEquip[4];
		tab2Item[0].isBuy = game.dataSave.isBuy[5];
		tab2Item[0].isEquipe = game.dataSave.isEquip[5];
		tab2Item[1].isBuy = game.dataSave.isBuy[6];
		tab2Item[1].isEquipe = game.dataSave.isEquip[6];
		tab2Item[2].isBuy = game.dataSave.isBuy[7];
		tab2Item[2].isEquipe = game.dataSave.isEquip[7];
		tab3Item[0].isBuy = game.dataSave.isBuy[8];
		tab3Item[0].isEquipe = game.dataSave.isEquip[8];
		tab3Item[1].isBuy = game.dataSave.isBuy[9];
		tab3Item[1].isEquipe = game.dataSave.isEquip[9];
		tab3Item[2].isBuy = game.dataSave.isBuy[10];
		tab3Item[2].isEquipe = game.dataSave.isEquip[10];
		tab3Item[3].isBuy = game.dataSave.isBuy[11];
		tab3Item[3].isEquipe = game.dataSave.isEquip[11];
		tab3Item[4].isBuy = game.dataSave.isBuy[12];
		tab3Item[4].isEquipe = game.dataSave.isEquip[12];
	}
	
	public ShopData(GameOS game)
	{
		this.game = game;
		
		tab = 1;
		item = 0;
		tab1Item = new InfoItem[5];
		tab1Item[0] = new InfoItem();
		
		tab1Item[1] = new InfoItem();
		tab1Item[1].price = 6000;
		
		tab1Item[2] = new InfoItem();
		tab1Item[2].price = 15000;
		
		tab1Item[3] = new InfoItem();
		tab1Item[3].price = 30000;
		
		tab1Item[4] = new InfoItem();
		tab1Item[4].price = 50000;
			
		tab2Item = new InfoItem[3];
		
		tab2Item[0] = new InfoItem();
		tab2Item[0].price = 15000;
			
		tab2Item[1] = new InfoItem();
		tab2Item[1].price = 5000;
		
		tab2Item[2] = new InfoItem();
		tab2Item[2].price = 15000;
		
		tab3Item = new InfoItem[5];
		
		tab3Item[0] = new InfoItem();
		tab3Item[0].price = 6500;
		
		tab3Item[1] = new InfoItem();
		tab3Item[1].price = 30000;
		
		tab3Item[2] = new InfoItem();
		tab3Item[2].price = 7500;
		
		tab3Item[3] = new InfoItem();
		tab3Item[3].price = 6000;
		
		tab3Item[4] = new InfoItem();
		tab3Item[4].price = 20000;
	}
}
