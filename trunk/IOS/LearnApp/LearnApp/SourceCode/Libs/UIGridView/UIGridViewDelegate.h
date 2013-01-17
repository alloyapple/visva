//
//  UIGridViewDelegate.h
//  foodling2
//
//  Created by Tanin Na Nakorn on 3/6/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
@class UIGridView;
@class UIGridViewCell;
@protocol UIGridViewDelegate


@optional
- (void) gridView:(UIGridView *)grid didSelect:(UIGridViewCell*)cell;


@required
- (CGFloat) gridView:(UIGridView *)grid widthForColumnAt:(int)columnIndex;
- (CGFloat) gridView:(UIGridView *)grid heightForRowAt:(int)rowIndex;

- (NSInteger) numberOfColumnsOfGridView:(UIGridView *) grid;
- (NSInteger) numberOfCellsOfGridView:(UIGridView *) grid;

- (UIGridViewCell *) gridView:(UIGridView *)grid cellForRowAt:(int)rowIndex AndColumnAt:(int)columnIndex;

@end

