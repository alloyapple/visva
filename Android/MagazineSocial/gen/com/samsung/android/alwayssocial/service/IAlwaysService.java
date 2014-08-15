/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Visva\\MagazineSocial\\src\\com\\samsung\\android\\alwayssocial\\service\\IAlwaysService.aidl
 */
package com.samsung.android.alwayssocial.service;
public interface IAlwaysService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.samsung.android.alwayssocial.service.IAlwaysService
{
private static final java.lang.String DESCRIPTOR = "com.samsung.android.alwayssocial.service.IAlwaysService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.samsung.android.alwayssocial.service.IAlwaysService interface,
 * generating a proxy if needed.
 */
public static com.samsung.android.alwayssocial.service.IAlwaysService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.samsung.android.alwayssocial.service.IAlwaysService))) {
return ((com.samsung.android.alwayssocial.service.IAlwaysService)iin);
}
return new com.samsung.android.alwayssocial.service.IAlwaysService.Stub.Proxy(obj);
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
case TRANSACTION_registerUiListener:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
com.samsung.android.alwayssocial.service.IUpdateUiCallback _arg2;
_arg2 = com.samsung.android.alwayssocial.service.IUpdateUiCallback.Stub.asInterface(data.readStrongBinder());
this.registerUiListener(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterUiNotify:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
this.unregisterUiNotify(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_requestSNS:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.util.Map _arg2;
java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
_arg2 = data.readHashMap(cl);
this.requestSNS(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_postSNS:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
this.postSNS(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_logoutSocial:
{
data.enforceInterface(DESCRIPTOR);
this.logoutSocial();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.samsung.android.alwayssocial.service.IAlwaysService
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
// Listener APIs

@Override public void registerUiListener(java.lang.String socialType, int ownerId, com.samsung.android.alwayssocial.service.IUpdateUiCallback listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(socialType);
_data.writeInt(ownerId);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerUiListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterUiNotify(java.lang.String socialType, int ownerId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(socialType);
_data.writeInt(ownerId);
mRemote.transact(Stub.TRANSACTION_unregisterUiNotify, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// SNS Request

@Override public void requestSNS(java.lang.String socialType, int requestDataType, java.util.Map param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(socialType);
_data.writeInt(requestDataType);
_data.writeMap(param);
mRemote.transact(Stub.TRANSACTION_requestSNS, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void postSNS(java.lang.String socialType, java.lang.String id, java.lang.String data, int postType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(socialType);
_data.writeString(id);
_data.writeString(data);
_data.writeInt(postType);
mRemote.transact(Stub.TRANSACTION_postSNS, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// SNS Logout

@Override public void logoutSocial() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_logoutSocial, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerUiListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterUiNotify = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_requestSNS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_postSNS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_logoutSocial = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
// Listener APIs

public void registerUiListener(java.lang.String socialType, int ownerId, com.samsung.android.alwayssocial.service.IUpdateUiCallback listener) throws android.os.RemoteException;
public void unregisterUiNotify(java.lang.String socialType, int ownerId) throws android.os.RemoteException;
// SNS Request

public void requestSNS(java.lang.String socialType, int requestDataType, java.util.Map param) throws android.os.RemoteException;
public void postSNS(java.lang.String socialType, java.lang.String id, java.lang.String data, int postType) throws android.os.RemoteException;
// SNS Logout

public void logoutSocial() throws android.os.RemoteException;
}
