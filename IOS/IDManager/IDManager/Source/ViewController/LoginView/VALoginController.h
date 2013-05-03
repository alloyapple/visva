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
    kTypeMasterPasswordReLogin,
    kTypeMasterPasswordChangePass,
    kTypeMasterPasswordChangePassCheck1,
    kTypeMasterPasswordChangePassCheck2,
}kTypeMasterPassword;

@protocol VALoginDelegate;
@interface VALoginController : UIViewController{
    
}
@property(nonatomic, assign)kTypeMasterPassword typeMasterPass;
@property(nonatomic, assign)UIViewController<VALoginDelegate> *loginDelegate;
@end

@protocol VALoginDelegate <NSObject>
-(void)loginViewDidLogin:(VALoginController*)vc;
@optional
-(void)loginViewDidCancel:(VALoginController*)vc;
@end
