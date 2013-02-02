//
//  VAImageCell.h
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface VAImageCell : UITableViewCell
@property (retain, nonatomic) IBOutlet UIImageView *imBackground;
@property (retain, nonatomic) IBOutlet UIImageView *imForceground;
-(void)setSpecialCellGroupSelected:(BOOL)selected;
@end
