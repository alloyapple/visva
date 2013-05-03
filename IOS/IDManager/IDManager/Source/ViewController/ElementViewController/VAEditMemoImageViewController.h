//
//  VAEditMemoImageViewController.h
//  IDManager
//
//  Created by tranduc on 3/23/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    kTypePanBorderNone,
    kTypePanBorderTop,
    kTypePanBorderBottom,
    kTypePanBorderLeft,
    kTypePanBorderRight,
    
}kTypePanBorder;

@protocol VAEditMemoImageDelegate;
@interface VAEditMemoImageViewController : UIViewController<UIGestureRecognizerDelegate,
UIImagePickerControllerDelegate>{
    float rotate;
}

@property(nonatomic, retain)NSString *sCurrentImagePath;
@property(nonatomic, assign)id<VAEditMemoImageDelegate>delegate;




@end

@protocol VAEditMemoImageDelegate <NSObject>
-(void)editMemoDidSave:(VAEditMemoImageViewController*)vc;
-(void)editMemoDidCancel:(VAEditMemoImageViewController*)vc;
@end

