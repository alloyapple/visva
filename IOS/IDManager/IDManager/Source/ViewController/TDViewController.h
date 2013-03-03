//
//  TDViewController.h
//  IDManager
//
//  Created by tranduc on 1/15/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VAElementViewController.h"
#import "TDWebViewController.h"
#import "VALoginController.h"


@interface TDViewController : UIViewController<UITableViewDataSource, UITableViewDelegate,
UISearchBarDelegate, UIAlertViewDelegate, VAElementViewDelegate, UIActionSheetDelegate,
TDWebViewDelegate, VALoginDelegate>

-(void)reLoadData;
-(void)destroyData;
@end
