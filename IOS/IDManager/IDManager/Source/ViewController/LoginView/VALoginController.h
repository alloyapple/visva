//
//  VALoginController.h
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    kTypeMasterPasswordFirst,
    kTypeMasterPasswordLogin,
    kTypeMasterPasswordChangePass
}kTypeMasterPassword;

@interface VALoginController : UIViewController
@property(nonatomic, readonly)kTypeMasterPassword typeMasterPass;

@end
