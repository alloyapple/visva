package vn.zgome.game.streetknight.menu.shop;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.util.ProcessTextUltil;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class AboutItemUi extends IEntityMenu{

	public AboutItemUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	Label textInfo;
	Table table;
	ScrollPane scroll;
	Label cost;
	String[][] details = new String[2][16];
	ImageButton buy;
	ImageButton equip;
	ImageButton unequip;
	@Override
	public void start () {		
		init();
		// TODO Auto-generated method stub
		LabelStyle style = new LabelStyle(game.freeFontTypeTool.variFont, Color.WHITE);
		textInfo = new Label("test", style);		
		textInfo.setText("");
		Table textTable = new Table(game.skin);
		scroll = new ScrollPane(textTable, game.skin, "trans");
		scroll.setBounds(game.X(10), game.Y(60), game.X(766-515-10), game.Y(150));
		scroll.setScrollingDisabled(true, false);
		table = game.stageHelper.createTable(game.X(515), game.Y(490-459), game.X(766-515), game.Y(459-170));
		
	   cost = game.stageHelper.createLabel("9000$", Color.ORANGE, game.X(5), game.Y(200));
	   table.addActor(cost);
	   equip = game.stageHelper.createImageButton(Asset.equip, Asset.equip, game.X(30), 0);
	   buy = game.stageHelper.createImageButton(Asset.buy, Asset.buy, game.X(30), 0);
	   unequip = game.stageHelper.createImageButton(Asset.unequip, Asset.unequip, game.X(30), 0);
	   table.addActor(equip);
	   table.addActor(unequip);
	   table.addActor(buy);
	   buy.addListener(new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				if(_screen.shopUi.shopData.tab == 1)
				{	
					if(_screen.shopUi.shopData.item>0)
					{
						if(game.dataSave.money >= _screen.shopUi.shopData.tab1Item[_screen.shopUi.shopData.item-1].price){
							game.dataSave.money -= _screen.shopUi.shopData.tab1Item[_screen.shopUi.shopData.item-1].price;
							game.dataSave.saveAll();
							_screen.shopUi.shopData.tab1Item[_screen.shopUi.shopData.item-1].isBuy = true;
							_screen.shopUi.refresh();
						}
						else
						{
							if(game.android!=null)
								game.android.showToast("Bạn không đủ xu để mua item này!");
						}
					}
				}
				if(_screen.shopUi.shopData.tab == 2)
				{	
					if(_screen.shopUi.shopData.item>0)
					{
						if(game.dataSave.money >= _screen.shopUi.shopData.tab2Item[_screen.shopUi.shopData.item-1].price){
							game.dataSave.money -= _screen.shopUi.shopData.tab2Item[_screen.shopUi.shopData.item-1].price;
							game.dataSave.saveAll();
						_screen.shopUi.shopData.tab2Item[_screen.shopUi.shopData.item-1].isBuy = true;
						_screen.shopUi.refresh();
						}
						else
						{
							if(game.android!=null)
								game.android.showToast("Bạn không đủ xu để mua item này!");
						}
					}
				}
				if(_screen.shopUi.shopData.tab == 3)
				{						
					if(_screen.shopUi.shopData.item>0)
					{
						if(game.dataSave.money >= _screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item-1].price){
							game.dataSave.money -= _screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item-1].price;							
						_screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item-1].isBuy = true;
						_screen.shopUi.refresh();
						if(_screen.shopUi.shopData.item==5)
						{							
							game.dataSave.coutHPBonus = 15;
							game.dataSave.saveAll();
						}
						game.dataSave.saveAll();
						}
						else
						{
							if(game.android!=null)
								game.android.showToast("Bạn không đủ xu để mua item này!");
						}
					}
				}
				saveAll();
			}
		});
	   
	   equip.addListener(new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				if(_screen.shopUi.shopData.tab == 1)
				{	
					if(_screen.shopUi.shopData.item>0)
					{
						_screen.shopUi.shopData.tab1Item[0].isEquipe = false;
						_screen.shopUi.shopData.tab1Item[1].isEquipe = false;
						_screen.shopUi.shopData.tab1Item[2].isEquipe = false;
						_screen.shopUi.shopData.tab1Item[3].isEquipe = false;
						_screen.shopUi.shopData.tab1Item[4].isEquipe = false;
						_screen.shopUi.shopData.tab1Item[_screen.shopUi.shopData.item-1].isEquipe = true;
						if(_screen.shopUi.shopData.item == 4)
						{
							game.gameScreen.nvc.delta = 1;
						}
						else if(_screen.shopUi.shopData.item == 5)
						{
							game.gameScreen.nvc.delta = 2;
						}
						else if(_screen.shopUi.shopData.item == 3)
							game.gameScreen.nvc.delta = 0;
						else 
							game.gameScreen.nvc.delta = -1;
						_screen.shopUi.refresh();
					}
				}
				if(_screen.shopUi.shopData.tab == 2)
				{	
					if(_screen.shopUi.shopData.item>0)
					{
						_screen.shopUi.shopData.tab2Item[0].isEquipe = false;
						_screen.shopUi.shopData.tab2Item[1].isEquipe = false;
						_screen.shopUi.shopData.tab2Item[2].isEquipe = false;
						_screen.shopUi.shopData.tab2Item[_screen.shopUi.shopData.item-1].isEquipe = true;
						_screen.shopUi.refresh();
					}
				}
				if(_screen.shopUi.shopData.tab == 3)
				{	
					if(_screen.shopUi.shopData.item>0)
					{
						_screen.shopUi.shopData.tab3Item[0].isEquipe = false;
						_screen.shopUi.shopData.tab3Item[1].isEquipe = false;
						_screen.shopUi.shopData.tab3Item[2].isEquipe = false;
						_screen.shopUi.shopData.tab3Item[3].isEquipe = false;
						_screen.shopUi.shopData.tab3Item[4].isEquipe = false;
						_screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item-1].isEquipe = true;
						_screen.shopUi.refresh();
					}
				}
				saveAll();
			}
		});
	   
	   unequip.addListener(new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				if(_screen.shopUi.shopData.tab == 1)
				{	
					if(_screen.shopUi.shopData.item>0)
					{
						_screen.shopUi.shopData.tab1Item[_screen.shopUi.shopData.item-1].isEquipe = false;
						_screen.shopUi.refresh();
					}
				}
				if(_screen.shopUi.shopData.tab == 2)
				{	
					if(_screen.shopUi.shopData.item>0)
					{
						_screen.shopUi.shopData.tab2Item[_screen.shopUi.shopData.item-1].isEquipe = false;
						_screen.shopUi.refresh();
					}
				}
				if(_screen.shopUi.shopData.tab == 3)
				{	
					if(_screen.shopUi.shopData.item>0)
					{
						_screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item-1].isEquipe = false;
						_screen.shopUi.refresh();
					}
				}
				saveAll();
			}
		});
	   textTable.add(textInfo).align(Align.left);
	   table.addActor(scroll);
		textInfo.setPosition(game.X(5), game.Y(120));
	}	
	
	public void refesh()
	{	
		if(_screen.shopUi.shopData.tab == 1){
			if((_screen.shopUi.shopData.tab-1)+_screen.shopUi.shopData.item==4)
			{
				//details[13].setVisible(true);
				textInfo.setText(details[game.dataSave.lang][13]);
			}		
			else if((_screen.shopUi.shopData.tab-1)+_screen.shopUi.shopData.item==5)
			{
				//details[14].setVisible(true);
				textInfo.setText(details[game.dataSave.lang][14]);
			}
			else
				textInfo.setText(details[game.dataSave.lang][(_screen.shopUi.shopData.tab-1)+_screen.shopUi.shopData.item]);
		}
		if(_screen.shopUi.shopData.tab == 2)
			//details[4+_screen.shopUi.shopData.item].setVisible(true);
			textInfo.setText(details[game.dataSave.lang][4+_screen.shopUi.shopData.item]);
		if(_screen.shopUi.shopData.tab == 3){
			//details[8+_screen.shopUi.shopData.item].setVisible(true);
			if(_screen.shopUi.shopData.item==5)
			{
				textInfo.setText(details[game.dataSave.lang][15]);
			}
			else
				textInfo.setText(details[game.dataSave.lang][8+_screen.shopUi.shopData.item]);
			
		}
		equip.setVisible(false);
		unequip.setVisible(false);
		buy.setVisible(false);
		cost.setVisible(false);
		
		if(_screen.shopUi.shopData.tab == 1)
		{	
			if(_screen.shopUi.shopData.item>0){
			if(_screen.shopUi.shopData.tab1Item[_screen.shopUi.shopData.item-1].isBuy)
			{
				if(_screen.shopUi.shopData.tab1Item[_screen.shopUi.shopData.item-1].isEquipe)
				{
					unequip.setVisible(true);
				}
				else					
				{	
					equip.setVisible(true);
				}
			}
			else
			{
				cost.setText(_screen.shopUi.shopData.tab1Item[_screen.shopUi.shopData.item-1].price+"");
				cost.setVisible(true);
				buy.setVisible(true);
			}
			}
		}
		if(_screen.shopUi.shopData.tab == 2)
		{	
			if(_screen.shopUi.shopData.item>0){
			if(_screen.shopUi.shopData.tab2Item[_screen.shopUi.shopData.item-1].isBuy)
			{
				if(_screen.shopUi.shopData.tab2Item[_screen.shopUi.shopData.item-1].isEquipe)
				{
					unequip.setVisible(true);
				}
				else					
				{	
					equip.setVisible(true);
				}
			}
			else
			{
				cost.setText(_screen.shopUi.shopData.tab2Item[_screen.shopUi.shopData.item-1].price+"");
				cost.setVisible(true);
				buy.setVisible(true);
			}
			}
		}
		if(_screen.shopUi.shopData.tab == 3)
		{	
			if(_screen.shopUi.shopData.item>0){
				if (_screen.shopUi.shopData.item != 5) {
					if (_screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item - 1].isBuy) {
						if (_screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item - 1].isEquipe) {
							unequip.setVisible(true);
						} else {
							equip.setVisible(true);
						}

					} else {
						cost.setText(_screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item - 1].price + "");
						cost.setVisible(true);
						buy.setVisible(true);
					}
				}
				else
				{
					if (_screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item - 1].isBuy) {
						if (_screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item - 1].isEquipe) {
							unequip.setVisible(true);
						} else {
							equip.setVisible(true);
						}

					} else {
						cost.setText(_screen.shopUi.shopData.tab3Item[_screen.shopUi.shopData.item - 1].price + "");
						cost.setVisible(true);
						buy.setVisible(true);
					}
				}
			}
		}
		int xCost = (int)((game.X(766-515) - cost.getTextBounds().width)/2);
		cost.setX(xCost);
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		
	}

	public void init()
	{
		details[0][0] = ProcessTextUltil.cutString(game, "Weapon sẽ thay đổi hình dạng của nhân vật và các tư thế võ thuật sẽ khác nhau.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][1] = ProcessTextUltil.cutString(game, "Bậc thầy về võ thuật , anh ấy là người mang lại hòa bình và trật tự cho đường phố Việt Nam, là một biểu tượng cho lòng quả cảm và bác ái, là tấm gương cho thế hệ trẻ noi theo",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][2] = ProcessTextUltil.cutString(game, "Nhanh và tấn công xa hơn, một võ sĩ Samurai kiếm đạo điêu luyện sẵn sàng xả thân bảo vệ lẽ phải. (Đặc biệt mạnh trong màn Nhật Bản, bạn được cộng 5 điểm khi tiêu diệt kẻ thù).",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][3] = ProcessTextUltil.cutString(game, "Võ sĩ giác đấu với thân hình cường tráng và sức khỏe vô địch, anh ta là nỗi khiếp sợ trên đấu trường. (Đặc biệt mạnh trong màn La mã, bạn được cộng thêm 5 điểm khi tiêu diệt kẻ thù).",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][13] = ProcessTextUltil.cutString(game, "Chiến binh Ai Cập Được biết đến như những tử thần cai quản hầm mộ của Pharaoh, sẵn sàng tiêu diệt kẻ bén mảng bằng những đòn đánh tàn ác nhất (Đặc biệt mạnh trong màn Ai Cập, bạn được cộng thêm 5 điểm khi tiêu diệt kẻ thù).",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][14] = ProcessTextUltil.cutString(game, "Chiến sĩ hải quân Việt Nam kiên cường bất khuất, sẵn sàng hy sinh quên mình cho sự bình yên của tổ quốc thân yêu ( Đặc biệt mạnh trong tất cả các màn chơi, bạn được cộng 20 điểm khi tiêu diệt kẻ thù).",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		
		details[0][4] = ProcessTextUltil.cutString(game, "Skill sẽ được kích hoạt khi bạn chạm vào nhân vật chính hoặc icon kỹ năng(skill).",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][5] = ProcessTextUltil.cutString(game, "Làm chậm kẻ thù tiến tới gần trong một khoảng thời gian nhất định.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][6] = ProcessTextUltil.cutString(game, "Tiêu diệt toàn bộ kẻ thù trên màn hình khi được kích hoạt.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][7] = ProcessTextUltil.cutString(game, "Trở thành bất tử, không một kẻ nào có thể khiến bạn bị thương trong một khoảng thời gian nhất định.",  game.X(766-515-20), game.freeFontTypeTool.variFont);

		details[0][8] = ProcessTextUltil.cutString(game, "Skill sẽ được kích hoạt khi bạn chạm vào nhân vật chính hoặc icon kỹ năng(skill).",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][9] = ProcessTextUltil.cutString(game, "Tăng cường thêm thời gian sử dụng kỹ năng (skill).",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][10] = ProcessTextUltil.cutString(game, "Số tiền mà bạn nhận được khi tiêu diệt kẻ thù sẽ tăng lên khi sử dụng bonus này.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][11] = ProcessTextUltil.cutString(game, "Cứ 40 giây trôi qua bạn được cộng thêm 01 HP (máu).",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][12] = ProcessTextUltil.cutString(game, "Skill(kỹ năng) sẽ được đếm ngược lại nhanh hơn, và bạn sẽ không phải đợi lâu để kích hoạt skill đó.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[0][15] = ProcessTextUltil.cutString(game, "Bạn được cộng thêm 15 máu nữa khi sử dụng bonus này.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		//details[0][6] = ProcessTextUltil.cutString(game, "Nhanh và tấn công xa hơn, một võ sĩ Samurai kiếm đạo điêu luyện sẵn sàng xả thân bảo vệ lẽ phải. (Đặc biệt mạnh trong màn Nhật Bản, bạn được cộng 5 điểm khi tiêu diệt kẻ thù).",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		//details[0][7] = ProcessTextUltil.cutString(game, "Võ sĩ giác đấu với thân hình cường tráng và sức khỏe vô địch, anh ta là nỗi khiếp sợ trên đấu trường. (Đặc biệt mạnh trong màn La mã, bạn được cộng thêm 5 điểm khi tiêu diệt kẻ thù).",  game.X(766-515-20), game.freeFontTypeTool.variFont);

		details[1][0] = ProcessTextUltil.cutString(game, " Equiped any weapon, it'll change your look and style of fighting.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][1] = ProcessTextUltil.cutString(game, "He's who bring justice for a street he's live.he's fearless.brave and very handsome",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][2] = ProcessTextUltil.cutString(game, "He's master of Katana sword, fast like the wind and people call him Samurai.(in Japan stage, you'll earn more score, 5 points add for each enemy killed)",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][3] = ProcessTextUltil.cutString(game, "The Gladiator. he's a champion in the Colosseum. The pround and icon for the brave of Rome.(in Rome stage. you'll earn more score. 5 point and for each enemy killed)",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][13] = ProcessTextUltil.cutString(game, " The most Satanic Warrior, the one who called Phraoh's guard.(in Egypt stage, you'll earn more score, 5 points add for each enemy)",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][14] = ProcessTextUltil.cutString(game, " The brave Navy army of VietNam who fight until the last breath and blood for his homeland. (you'll earn more score for all of stage you play, 20 points add for each enemy killed)",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		
		details[1][4] = ProcessTextUltil.cutString(game, "If you equiped your skill. it'll only active when you tap on Icon Skill or tap on your main character.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][5] = ProcessTextUltil.cutString(game, "Slow all enemy movement come nearby in stage for amout of time.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][6] = ProcessTextUltil.cutString(game, "Destroy all enemies on screen when player active this skill.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][7] = ProcessTextUltil.cutString(game, "Become untouchable for amount of time. Player can't lose health when active this skill.",  game.X(766-515-20), game.freeFontTypeTool.variFont);

		details[1][8] = ProcessTextUltil.cutString(game, "When you equiped your Bonus, it's effect all the time without active.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][9] = ProcessTextUltil.cutString(game, "This Bonus will add more effect time for you Skill",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][10] = ProcessTextUltil.cutString(game, "You'll earn more money when busy kicking enemy's ass.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][11] = ProcessTextUltil.cutString(game, "Every 40 second, this Bonus will auto add 1 Hp to health bar.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][12] = ProcessTextUltil.cutString(game, "This Bonus will cooldown the reload time of Skill, and you don't have to wait too long.",  game.X(766-515-20), game.freeFontTypeTool.variFont);
		details[1][15] = ProcessTextUltil.cutString(game, "Add more 15 hp to your hp bar when you’re using this bonus.",  game.X(766-515-20), game.freeFontTypeTool.variFont);

	}
	
	public void saveAll()
	{
		for(int i=0;i<5;i++)
		{
			game.dataSave.isBuy[i] = _screen.shopUi.shopData.tab1Item[i].isBuy;
			game.dataSave.isEquip[i] = _screen.shopUi.shopData.tab1Item[i].isEquipe;
		}
		for(int i=0;i<3;i++)
		{
			game.dataSave.isBuy[i+5] = _screen.shopUi.shopData.tab2Item[i].isBuy;
			game.dataSave.isEquip[i+5] = _screen.shopUi.shopData.tab2Item[i].isEquipe;
		}
		for(int i=0;i<5;i++)
		{
			game.dataSave.isBuy[i+8] = _screen.shopUi.shopData.tab3Item[i].isBuy;
			game.dataSave.isEquip[i+8] = _screen.shopUi.shopData.tab3Item[i].isEquipe;
		}
		game.dataSave.saveAll();
	}
}


