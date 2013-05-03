//
//  VAElementOptionController.m
//  IDManager
//
//  Created by tranduc on 4/11/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAElementOptionController.h"
#import "VAPassword.h"
#import "TDCommonLibs.h"
#import "VAElementViewController.h"
#import "TDAlert.h"

@interface VAElementOptionController ()<UITableViewDataSource, UITableViewDelegate>

@end

@implementation VAElementOptionController

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
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return _selectedElement.aPasswords.count + 2;
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    int row = indexPath.row;
    NSString *title = @"";
    if (row < _selectedElement.aPasswords.count) {
        title = [(VAPassword*)[_selectedElement.aPasswords objectAtIndex:row] sTitleNameId];
    }else if (row == _selectedElement.aPasswords.count){
        title = TDLocStrOne(@"Web");
    }else{
        title = TDLocStrOne(@"Edit");
    }
    UITableViewCell *cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:@""] autorelease];
    UIView *view = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 44)] autorelease];
    UIImageView *imv = [[UIImageView alloc] initWithFrame:CGRectMake(0, 2, 320, 40)];
    imv.image = [UIImage imageNamed:@"copy-bottun_push.png"];
    [view addSubview:imv];
    view.backgroundColor = [UIColor clearColor];
    cell.backgroundView = view;
    
    UILabel *lb = [[[UILabel alloc] initWithFrame:CGRectMake(0, 2, 320, 40)] autorelease];
    lb.text = title;
    lb.backgroundColor = [UIColor clearColor];
    lb.textAlignment = NSTextAlignmentCenter;
    lb.textColor = [UIColor whiteColor];
    [cell.contentView addSubview:lb];
    
    return cell;
}
-(void)pasteClipBoard:(NSString*)str{
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    pasteboard.persistent = YES;
    pasteboard.string = str;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    int row = indexPath.row;
    NSString *title = @"";
    if (row < _selectedElement.aPasswords.count) {
        VAPassword *pa = (VAPassword*)[_selectedElement.aPasswords objectAtIndex:row];
        title = [NSString stringWithFormat:@"%@ %@", [pa sTitleNameId], TDLocStrOne(@"copied")];
        [self pasteClipBoard:pa.sPassword];
        [TDAlert showMessageWithTitle:title message:nil delegate:self];
    }else if (row == _selectedElement.aPasswords.count){
        TDLOG(@"Start web");
        TDWebViewController *web = [[[TDWebViewController alloc] initWithNibName:@"TDWebViewController" bundle:nil]autorelease];
        web.sUrlStart = _selectedElement.sUrl;
        web.bIsUseJogDial = YES;
        web.listPWID = _selectedElement.aPasswords;
        web.sNote = _selectedElement.sNote;
        web.webDelegate = _elementDelegate;
        web.iTag = 0;
        UINavigationController *navi = [self.navigationController retain];
        [navi popViewControllerAnimated:NO];
        [navi pushViewController:web animated:YES];
        [navi release];
    }else{
        VAElementViewController *vc = [[[VAElementViewController alloc] initWithNibName:@"VAElementViewController" bundle:nil]autorelease];
        vc.currentElement = _selectedElement;
        vc.elementDelegate = _elementDelegate;
        vc.isEditMode = YES;
        
        UINavigationController *navi = [self.navigationController retain];
        [navi popViewControllerAnimated:NO];
        [navi pushViewController:vc animated:YES];
        [navi release];
    }
}




- (IBAction)btBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
