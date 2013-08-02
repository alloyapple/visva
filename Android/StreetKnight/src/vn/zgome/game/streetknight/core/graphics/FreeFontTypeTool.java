package vn.zgome.game.streetknight.core.graphics;

import vn.zgome.game.streetknight.core.GameOS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;

public class FreeFontTypeTool {
	
	public BitmapFont normalFont;
	public BitmapFont variFont;
	public BitmapFont variSmallFont;
	public BitmapFont variBigFont;
	public BitmapFont variBiggerFont;
	public GameOS game;	
	
	public FreeFontTypeTool(GameOS game)
	{
		this.game = game;
	}
	
	public void loadFont()
	{
		String chars="AÁẠÃÀẢĂẲẮẰẴẶÂẨẤẦẪẬBCDĐEẺÉÈẼẸÊỂẾỀỄỆGHIỈÍÌĨỊKLMNOỎÓÒÕỌÔỔỐỒỖỘƠỞỚỜỠỢPQRSTUỦÚÙŨỤƯỬỨỪỮỰVXYJWZaảáàãạăẳắằẵặâẩấầẫậbcdđeẻéèẽẹêểếềễệghiỉíìĩịklmnoỏóòõọôổốồỗộơởớờỡợpqrstuủúùũụưửứừữựvxyỷýỳỹỵfjwz0123456789\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*";
		if(normalFont == null){			
			FileHandle fontFile = Gdx.files.internal("data/Magnum.TTF");
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);	
			float scale = game.screenInfo.scaleX;						
			FreeTypeBitmapFontData fontData = generator.generateData((int)(Math.ceil(40*scale)), chars, false);
			generator.dispose();			
			normalFont = new BitmapFont(fontData, fontData.getTextureRegion(), false);
		}
		if(variFont == null){			
			FileHandle fontFile1 = Gdx.files.internal("data/SEGUISB.TTF");
			FreeTypeFontGenerator generator1 = new FreeTypeFontGenerator(fontFile1);	
			float scale = game.screenInfo.scaleX;						
			FreeTypeBitmapFontData fontData1 = generator1.generateData((int)(Math.ceil(20*scale)), chars, false);
			generator1.dispose();			
			variFont = new BitmapFont(fontData1, fontData1.getTextureRegion(), false);
		}
		if(variSmallFont == null){			
			FileHandle fontFile2 = Gdx.files.internal("data/SEGUISB.TTF");
			FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(fontFile2);	
			float scale = game.screenInfo.scaleX;						
			FreeTypeBitmapFontData fontData2 = generator2.generateData((int)(Math.ceil(16*scale)), chars, false);
			generator2.dispose();			
			variSmallFont = new BitmapFont(fontData2, fontData2.getTextureRegion(), false);
		}
		if(variBigFont == null){			
			FileHandle fontFile3 = Gdx.files.internal("data/SEGUISB.TTF");
			FreeTypeFontGenerator generator3 = new FreeTypeFontGenerator(fontFile3);	
			float scale = game.screenInfo.scaleX;						
			FreeTypeBitmapFontData fontData3 = generator3.generateData((int)(Math.ceil(25*scale)), chars, false);
			generator3.dispose();			
			variBigFont = new BitmapFont(fontData3, fontData3.getTextureRegion(), false);
		}
		if(variBiggerFont == null){			
			FileHandle fontFile4 = Gdx.files.internal("data/SEGUISB.TTF");
			FreeTypeFontGenerator generator4 = new FreeTypeFontGenerator(fontFile4);	
			float scale = game.screenInfo.scaleX;						
			FreeTypeBitmapFontData fontData4 = generator4.generateData((int)(Math.ceil(35*scale)), chars, false);
			generator4.dispose();			
			variBiggerFont = new BitmapFont(fontData4, fontData4.getTextureRegion(), false);
		}
	}
}
