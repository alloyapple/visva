//
//  VAEditImageViewController.h
//  IDManager
//
//  Created by tranduc on 3/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TDWebViewController.h"

typedef enum {
    kTypeChooseIcon,
    kTypeChooseMemoImage
}kTypeChoosen;
@protocol VAEditImageDelegate;
@interface VAEditImageViewController : UIViewController<UIImagePickerControllerDelegate, UIScrollViewDelegate,
    TDWebViewDelegate, UIGestureRecognizerDelegate>

@property(nonatomic, retain)NSString *sCurrentImagePath;
@property(nonatomic, readonly)BOOL isAddNewImage;
@property(nonatomic, assign)int tag;
@property(nonatomic, assign)kTypeChoosen type;
@property(nonatomic, assign)id<VAEditImageDelegate> delegate;
@end

@protocol VAEditImageDelegate <NSObject>

-(void)editImageAccept:(VAEditImageViewController*)vc;
-(void)editImageCancel:(VAEditImageViewController *)vc;

@end
