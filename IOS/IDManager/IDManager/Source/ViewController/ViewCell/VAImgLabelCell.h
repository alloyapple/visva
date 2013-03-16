//
//  VAImgLabelCell.h
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface VAImgLabelCell : UITableViewCell
@property (retain, nonatomic) IBOutlet UIImageView *imBackgroud;
@property (retain, nonatomic) IBOutlet UILabel *lbTitle;
@property (retain, nonatomic) IBOutlet UIView *bgView;
@property (retain, nonatomic) IBOutlet UIView *vEdit;

-(void)setCellGroupSelected:(BOOL)selected;
@end
