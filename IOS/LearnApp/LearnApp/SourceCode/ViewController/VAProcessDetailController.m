//
//  VAProcessDetailController.m
//  LearnApp
//
//  Created by Tam Nguyen on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAProcessDetailController.h"

@interface VAProcessDetailController ()

@end

@implementation VAProcessDetailController

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
    self.imageView = [[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_edit_process.png"]] autorelease];
    self.view= self.imageView;
	// Do any additional setup after loading the view.
}
- (void)viewDidUnload {
    [_imageView release];
}
- (void)dealloc {
    [self setImageView:nil];
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
