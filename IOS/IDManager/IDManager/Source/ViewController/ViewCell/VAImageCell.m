//
//  VAImageCell.m
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAImageCell.h"
#import "TDCommonLibs.h"
@implementation VAImageCell
-(void)awakeFromNib{
    [super awakeFromNib];
    TDLOG(@"bg = %@, bgSelf = %@", _bgView, self.backgroundView);
    //self.backgroundView = _bgView;
    //self.selectedBackgroundView = _bgView;
    
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
-(void)setSpecialCellGroupSelected:(BOOL)selected{
    if (selected) {
        self.imBackground.image = [UIImage imageNamed:@"folder-s-select.png"];
    }else{
        self.imBackground.image = [UIImage imageNamed:@"folder-s-common.png"];
    }
}
- (void)dealloc {
    [_imBackground release];
    [_imForceground release];
    [_bgView release];
    [super dealloc];
}
@end
