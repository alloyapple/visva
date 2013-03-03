//
//  VAImgLabelCell.m
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAImgLabelCell.h"
#import "TDCommonLibs.h"
@implementation VAImgLabelCell
-(void)awakeFromNib{
    [super awakeFromNib];
    TDLOG(@"bg = %@, bgSelf = %@", _bgView, self.backgroundView);
    self.backgroundView = _bgView;
    
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)dealloc {
    [_imBackgroud release];
    [_lbTitle release];
    [_bgView release];
    [super dealloc];
}
- (void)setCellGroupSelected:(BOOL)selected{
    if (selected) {
        _imBackgroud.image = [UIImage imageNamed:@"folder-s-common.png"];
    }else{
        _imBackgroud.image = [UIImage imageNamed:@"folder-common.png"];
    }
}
@end
