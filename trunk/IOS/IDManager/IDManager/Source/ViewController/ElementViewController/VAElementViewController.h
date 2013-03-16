//
//  ElementViewController.h
//  IDManager
//
//  Created by tranduc on 2/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VAElementId.h"
#import "TDWebViewController.h"
#import "VAChooseIconViewController.h"
#import "VAEditImageViewController.h"

@class VAPasswordIDView;
@class VAPassword;

@protocol VAPasswordIDViewDelegate
-(void)genTextFor:(VAPasswordIDView*)view;
@end
@interface VAPasswordIDView : UITableViewCell<UITextFieldDelegate>
@property(nonatomic, retain)IBOutlet UITextField *vPassword;
@property(nonatomic, retain)IBOutlet UITextField *vId;
@property(nonatomic, retain)UITextField *selectedField;
@property(nonatomic, retain)IBOutlet VAPassword *currentPwId;
@property(nonatomic, assign)IBOutlet id<VAPasswordIDViewDelegate> delegate;

- (void)updatePWIDFromView;
-(void)updatePWIDToView;

@end

@protocol VAElementViewDelegate;
@interface VAElementViewController : UIViewController<TDWebViewDelegate, UITextFieldDelegate, VAChooseIconDelegate, VAPasswordIDViewDelegate,
    UITableViewDataSource, UITableViewDelegate, UITextViewDelegate,
    VAEditImageDelegate>{
    BOOL _isFirstKeyboardShown;
}
@property(nonatomic, assign)IBOutlet id<VAElementViewDelegate> elementDelegate;
@property(nonatomic, retain)VAElementId *currentElement;
@property(nonatomic, assign)BOOL isEditMode;

@end

@protocol VAElementViewDelegate
-(void)elementViewDidSave:(VAElementViewController*)controller;
-(void)elementViewDidCancel:(VAElementViewController *)controller;

@end