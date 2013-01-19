//
//  TDMoveObjsViewController.h
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    kTypeTouchDrawLine,
    kTypeTouchMoveObj
}kTypeTouch;

@protocol TDMoveObjsDelegate;
@interface TDMoveObjsViewController : UIViewController<UIGestureRecognizerDelegate>{
    
}
@property(nonatomic, assign)id<TDMoveObjsDelegate> moveObjDelegate;
@property(nonatomic, assign)kTypeTouch typeTouch;
-(id)initWithFrame:(CGRect)frame ListPiece:(NSArray *)arr;

@end
@protocol TDMoveObjsDelegate <NSObject>
-(UIView*)getAddView:(TDMoveObjsViewController*)moveController;
@end

@interface TDDrawView : UIView
-(void)add:(UIView*)v1 andView:(UIView*)v2;
@property(nonatomic, assign)BOOL isDrawing;
@property(nonatomic, assign)CGPoint touchDrawPoint;
@property(nonatomic, assign)CGPoint touchBeginPoint;
@end
