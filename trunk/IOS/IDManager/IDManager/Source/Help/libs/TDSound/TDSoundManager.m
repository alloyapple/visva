//
//  SoundManager.m
//  TrieuPhuOnline
//
//  Created by user on 5/3/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "TDSoundManager.h"
#import "TDDatabase.h"
#import "TDLog.h"
#import "SimpleAudioEngine.h"

static TDSoundManager *instanceOfSoundManager;
@implementation TDSoundManager
#pragma mark - init
-(void)dealloc{
    TDLOGFUNC();
    [_bgMusic release];
    [super dealloc];
}

+(TDSoundManager *)shareSMN{
    if (instanceOfSoundManager == nil) {
        instanceOfSoundManager = [[TDSoundManager alloc] init];
    }
    return instanceOfSoundManager;
}
#pragma mark volume
-(void)updateVolumeBg{
    //[_bgMusic setVolume:[GameConfig shareGameConfig].bgVolume];
}
-(void)updateEffectVolumn{
    //[_longEffect setVolume:[GameConfig shareGameConfig].effectVolume];
}

#pragma mark bgmusic
-(void)playBackGroundMusic:(NSString *)path loop:(BOOL)loop{
    if (_bgMusic != nil) {
        if ([_bgMusic isPlaying]) {
            [_bgMusic stop];
        }
        [_bgMusic release];
    }
    NSURL *pathURL = [NSURL fileURLWithPath:[[NSBundle mainBundle]pathForResource:path ofType:@""]];
    _bgMusic = [[AVAudioPlayer alloc] initWithContentsOfURL:pathURL error:nil];
    if (loop) {
        [_bgMusic setNumberOfLoops:-1];
    }else{
        [_bgMusic setNumberOfLoops:0];
    }
    [self updateVolumeBg];
    [_bgMusic play];
}
-(void)playBackGroundMusic:(NSString *)path target:(id)target doWhenDone:(SEL)selector withObject:(id)object{
    [self playBackGroundMusic:path loop:NO];
    [target performSelector:selector withObject:object afterDelay:_bgMusic.duration];
    //NSLog(@"%f", _bgMusic.duration);
}
-(void)playBackGroundMusic{
    if (_bgMusic != nil && ![_bgMusic isPlaying]) {
        [_bgMusic play];
    }
}
-(void)pauseBackGroundMusic{
    if (_bgMusic != nil && [_bgMusic isPlaying]) {
        [_bgMusic pause];
    }
}
-(void)resumeBackGroundMusic{
    if (_bgMusic != nil && ![_bgMusic isPlaying]) {
        [_bgMusic play];
    }
}
-(void)stopBackGroundMusic{
    if (_bgMusic != nil && [_bgMusic isPlaying]) {
        [_bgMusic stop];
    }
}
#pragma mark - long effect
-(void)cancelLongEffect{
    if (_longEffect != nil) {
        if ([_longEffect isPlaying]) {
            [_longEffect stop];
            if(_targetLongEffect != nil){
                [NSObject cancelPreviousPerformRequestsWithTarget:_targetLongEffect selector:_selectorLongEffect object:_objectLongEffect];
            }
        }
        [_longEffect release];
        _longEffect = nil;
    }
}
-(void)playLongEffect:(NSString *)path target:(id)target doWhenDone:(SEL)selector withObject:(id)object{
    [self cancelLongEffect];
    NSURL *pathURL = [NSURL fileURLWithPath:[[NSBundle mainBundle]pathForResource:path ofType:@""]];
    _longEffect = [[AVAudioPlayer alloc] initWithContentsOfURL:pathURL error:nil];
    [_longEffect setNumberOfLoops:0];
    [self updateEffectVolumn];
    [_longEffect play];
    _targetLongEffect = target;
    _selectorLongEffect = selector;
    _objectLongEffect = object;
    [target performSelector:selector withObject:object afterDelay:_longEffect.duration];
}

-(void)playShortEffectWithPath:(NSString *)path{
    NSURL *pathURL = [NSURL fileURLWithPath:path];
    AVAudioPlayer *shortPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:pathURL error:nil];
    [shortPlayer setNumberOfLoops:0];
    [shortPlayer play];
}

+(void)simplePlayShortEffect:(NSString*)path
{
    [[SimpleAudioEngine sharedEngine] playEffect:path];
}
+(void)playShortEffectWithFile:(NSString *)file{
    NSString *path = [TDDatabase pathInBundle:file];
    [TDSoundManager simplePlayShortEffect:path];
}
@end
