//
//  VAVSMDrawViewController.h
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TDMoveObjsViewController.h"
typedef enum {
    kTypeDrawObjectMove=1,
    kTypeDrawObjectBox1=2,
    kTypeDrawObjectBox2=3,
    kTypeDrawObjectBox3=4,
    kTypeDrawObjectRect=5,
    kTypeDrawObjectLine=6,
}kTypeDrawObject;
@interface VAVSMDrawViewController : UIViewController<TDMoveObjsDelegate>
{
    kTypeDrawObject _currentType;
    UIButton *_selectedButton;
}
@end
