package com.gmail.gustgamer29.reflection;

import com.gmail.gustgamer29.reflection.v1_7_R3.VersionProtocolControl1_7_R3;
import com.gmail.gustgamer29.reflection.v1_7_R4.VersionProtocolControl1_7_R4;
import com.gmail.gustgamer29.reflection.v1_8_R1.VersionProtocolControl1_8_R1;
import com.gmail.gustgamer29.reflection.v1_8_R2.VersionProtocolControl1_8_R2;
import com.gmail.gustgamer29.reflection.v1_8_R3.VersionProtocolControl1_8_R3;

import javax.inject.Inject;

public class ReflectionUtils {

    private String version;
    private VersionProtocol versionProtocol;
    private NBTVersion nbtVersion;

    /**
     *
     * @param version craftbukkit version
     */
    @Inject
    ReflectionUtils(String version) throws UnSupportedVersion {
        this.version = version;
        configureVersionProtocol();
    }

    private void configureVersionProtocol() throws UnSupportedVersion {
        SupportedVersionsAtNow v = SupportedVersionsAtNow.valueOf(this.version);
        switch (v){
            case v1_7_R3:
                this.versionProtocol = VersionProtocolControl1_7_R3.getInstance();
                this.nbtVersion = VersionProtocolControl1_7_R3.getInstance();
                break;
            case v1_7_R4:
                this.versionProtocol = VersionProtocolControl1_7_R4.getInstance();
                this.nbtVersion = VersionProtocolControl1_7_R4.getInstance();
                break;
            case v1_8_R1:
                this.versionProtocol = VersionProtocolControl1_8_R1.getInstance();
                break;
            case v1_8_R2:
                this.versionProtocol = VersionProtocolControl1_8_R2.getInstance();
                break;
            case v1_8_R3:
                this.versionProtocol = VersionProtocolControl1_8_R3.getInstance();
                this.nbtVersion = VersionProtocolControl1_8_R3.getInstance();
                break;
            default:
                throw new UnSupportedVersion("The server is running with a version not supported for this plugin!");
        }
    }

    public VersionProtocol getVersionProtocol(){
        return this.versionProtocol;
    }

    public NBTVersion getNbtVersion() {
        return nbtVersion;
    }

    public class UnSupportedVersion extends Exception {
         UnSupportedVersion(String message) {
            super(message);
        }
    }
}
