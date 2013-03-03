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
    kTypeMasterPasswordChangePass
}kTypeMasterPassword;

@protocol VALoginDelegate;
@interface VALoginController : UIViewController{
    
}
@property(nonatomic, assign)kTypeMasterPassword typeMasterPass;
@property(nonatomic, assign)id<VALoginDelegate> loginDelegate;
@end

@protocol VALoginDelegate <NSObject>
-(void)loginViewDidLogin:(VALoginController*)vc;

@end
