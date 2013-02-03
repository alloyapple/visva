//
//  TDViewController.h
//  IDManager
//
//  Created by tranduc on 1/15/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VAElementViewController.h"
@interface TDViewController : UIViewController<UITableViewDataSource, UITableViewDelegate,
UISearchBarDelegate, UIAlertViewDelegate, VAElementViewDelegate>

-(void)reLoadData;
@end
