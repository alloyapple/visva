//
//  VAVSMDrawViewController.m
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAVSMDrawViewController.h"
#import "TDCommonLibs.h"

@interface VAVSMDrawViewController ()
@property(nonatomic, assign)TDMoveObjsViewController *moveObj;
- (IBAction)btDrawObject:(UIButton *)sender;

@end

@implementation VAVSMDrawViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    NSArray *listPiece = nil;
    CGSize size = self.view.frame.size;
    CGRect rect = TDRectFromSizeScaleXY(size, 1, 0.6);
	_moveObj = [[[TDMoveObjsViewController alloc] initWithFrame:rect ListPiece:listPiece] autorelease];
    _moveObj.moveObjDelegate = self;
    _moveObj.typeTouch = kTypeTouchMoveObj;
    [self addChildViewController:_moveObj];
    [self.view addSubview:_moveObj.view];
    
    
}
-(NSString *)imageNameFor:(kTypeDrawObject)type{
    switch (type) {
        case kTypeDrawObjectMove:
        case kTypeDrawObjectLine:
            return nil;
            break;
        case kTypeDrawObjectBox1:
            return @"box1.png";
            break;
        case kTypeDrawObjectBox2:
            return @"box2.png";
            break;
        case kTypeDrawObjectBox3:
            return @"box3.png";
            break;
        case kTypeDrawObjectRect:
            return @"rect.png";
            break;
        default:
            return nil;
            break;
    }
}
-(UIView*)getAddView:(TDMoveObjsViewController *)moveController{
    NSString *str = [self imageNameFor:_currentType];
    if (str) {
        UIImageView *im = [[UIImageView alloc] initWithImage:[UIImage imageNamed:str]];
        im.frame = TDRectFromSize(self.view.frame.size, 0.1);
        im.contentMode = UIViewContentModeScaleAspectFit;
        return im;
    }
    return nil;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    
}

- (IBAction)btDrawObject:(UIButton *)sender {
    [_selectedButton setEnabled:YES];
    _selectedButton = sender;
    [_selectedButton setEnabled:NO];
    kTypeDrawObject type = sender.tag;
    _currentType = type;
    if (_currentType == kTypeDrawObjectLine) {
        _moveObj.typeTouch = kTypeTouchDrawLine;
    }else{
        _moveObj.typeTouch = kTypeTouchMoveObj;
    }
}
@end
