/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Visva\\MagazineSocial\\src\\com\\samsung\\android\\alwayssocial\\service\\IUpdateUiCallback.aidl
 */
package com.samsung.android.alwayssocial.service;
public interface IUpdateUiCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.samsung.android.alwayssocial.service.IUpdateUiCallback
{
private static final java.lang.String DESCRIPTOR = "com.samsung.android.alwayssocial.service.IUpdateUiCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.samsung.android.alwayssocial.service.IUpdateUiCallback interface,
 * generating a proxy if needed.
 */
public static com.samsung.android.alwayssocial.service.IUpdateUiCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.samsung.android.alwayssocial.service.IUpdateUiCallback))) {
return ((com.samsung.android.alwayssocial.service.IUpdateUiCallback)iin);
}
return new com.samsung.android.alwayssocial.service.IUpdateUiCallback.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onUpdateUiByStoryItem:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
boolean _arg2;
_arg2 = (0!=data.readInt());
this.onUpdateUiByStoryItem(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onUpdateErrorUi:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
boolean _arg2;
_arg2 = (0!=data.readInt());
this.onUpdateErrorUi(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onUpdateCommnetInforToUi:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.util.List<com.samsung.android.alwayssocial.service.CommentParcelableObject> _arg2;
_arg2 = data.createTypedArrayList(com.samsung.android.alwayssocial.service.CommentParcelableObject.CREATOR);
java.lang.String _arg3;
_arg3 = data.readString();
this.onUpdateCommnetInforToUi(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_onUpdateLikeCommentCount:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
this.onUpdateLikeCommentCount(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_receiveStoryItems:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.util.List<com.samsung.android.alwayssocial.service.StoryItemUnit> _arg2;
_arg2 = data.createTypedArrayList(com.samsung.android.alwayssocial.service.StoryItemUnit.CREATOR);
boolean _arg3;
_arg3 = (0!=data.readInt());
boolean _arg4;
_arg4 = (0!=data.readInt());
this.receiveStoryItems(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.samsung.android.alwayssocial.service.IUpdateUiCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
// callback from service to activity about feed

@Override public void onUpdateUiByStoryItem(java.lang.String socialType, int feedType, boolean isResponseByRefresh) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(socialType);
_data.writeInt(feedType);
_data.writeInt(((isResponseByRefresh)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_onUpdateUiByStoryItem, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUpdateErrorUi(java.lang.String socialType, int errorCode, boolean isResponseByRefresh) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(socialType);
_data.writeInt(errorCode);
_data.writeInt(((isResponseByRefresh)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_onUpdateErrorUi, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// callback from service to activity about comment information

@Override public void onUpdateCommnetInforToUi(java.lang.String socialType, int requestType, java.util.List<com.samsung.android.alwayssocial.service.CommentParcelableObject> comments, java.lang.String nextPage) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(socialType);
_data.writeInt(requestType);
_data.writeTypedList(comments);
_data.writeString(nextPage);
mRemote.transact(Stub.TRANSACTION_onUpdateCommnetInforToUi, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUpdateLikeCommentCount(java.lang.String socialType, int requestType, int count, int isUserLike) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(socialType);
_data.writeInt(requestType);
_data.writeInt(count);
_data.writeInt(isUserLike);
mRemote.transact(Stub.TRANSACTION_onUpdateLikeCommentCount, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// callback from service to transfer story object to activity

@Override public void receiveStoryItems(java.lang.String socialType, int feedType, java.util.List<com.samsung.android.alwayssocial.service.StoryItemUnit> Items, boolean isResponseByRefresh, boolean isFirstTimeUpdate) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(socialType);
_data.writeInt(feedType);
_data.writeTypedList(Items);
_data.writeInt(((isResponseByRefresh)?(1):(0)));
_data.writeInt(((isFirstTimeUpdate)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_receiveStoryItems, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onUpdateUiByStoryItem = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onUpdateErrorUi = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onUpdateCommnetInforToUi = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onUpdateLikeCommentCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_receiveStoryItems = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
// callback from service to activity about feed

public void onUpdateUiByStoryItem(java.lang.String socialType, int feedType, boolean isResponseByRefresh) throws android.os.RemoteException;
public void onUpdateErrorUi(java.lang.String socialType, int errorCode, boolean isResponseByRefresh) throws android.os.RemoteException;
// callback from service to activity about comment information

public void onUpdateCommnetInforToUi(java.lang.String socialType, int requestType, java.util.List<com.samsung.android.alwayssocial.service.CommentParcelableObject> comments, java.lang.String nextPage) throws android.os.RemoteException;
public void onUpdateLikeCommentCount(java.lang.String socialType, int requestType, int count, int isUserLike) throws android.os.RemoteException;
// callback from service to transfer story object to activity

public void receiveStoryItems(java.lang.String socialType, int feedType, java.util.List<com.samsung.android.alwayssocial.service.StoryItemUnit> Items, boolean isResponseByRefresh, boolean isFirstTimeUpdate) throws android.os.RemoteException;
}
