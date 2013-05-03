//
//  VAEmailViewController.h
//  IDManager
//
//  Created by tranduc on 3/23/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    kTypeEmailVCRegister,
    kTypeEmailVCChange
}kTypeEmailVC;

@interface VAEmailViewController : UIViewController<UITextFieldDelegate, UIAlertViewDelegate>
@property(nonatomic, assign)kTypeEmailVC type;

@end
