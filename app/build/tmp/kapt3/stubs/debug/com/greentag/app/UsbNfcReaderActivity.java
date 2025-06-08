package com.greentag.app;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0002J\u0012\u0010\u000b\u001a\u00020\n2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0014J\b\u0010\u000e\u001a\u00020\nH\u0014J\b\u0010\u000f\u001a\u00020\nH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/greentag/app/UsbNfcReaderActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "device", "Landroid/hardware/usb/UsbDevice;", "reader", "Lcom/acs/smartcard/Reader;", "usbManager", "Landroid/hardware/usb/UsbManager;", "connectToUsbDevice", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "readCard", "app_debug"})
public final class UsbNfcReaderActivity extends androidx.appcompat.app.AppCompatActivity {
    private android.hardware.usb.UsbManager usbManager;
    private com.acs.smartcard.Reader reader;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.usb.UsbDevice device;
    
    public UsbNfcReaderActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void connectToUsbDevice() {
    }
    
    private final void readCard() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
}