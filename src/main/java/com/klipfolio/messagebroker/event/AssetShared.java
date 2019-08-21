package com.klipfolio.messagebroker.event;

import com.klipfolio.messagebroker.core.exception.EventInvalidFieldException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssetShared implements Event {

    private static final int EVENT_VERSION = 1;

    private String assetId;
    private String assetName;
    private String assetTypeDisplayName;
    private String rawAssetType;
    private String shareTargetType;
    private String shareTargetId;
    private String shareRightType;
    private String sharerId;

    @Override
    public String getName() {
        return SupportedEvent.ASSET_SHARED.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }

    @Override
    public Map<String, Object> getPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("assetId", assetId);
        payload.put("assetName", assetName);
        payload.put("assetTypeDisplayName", assetTypeDisplayName);
        payload.put("rawAssetType", rawAssetType);
        payload.put("shareTargetType", shareTargetType);
        payload.put("shareTargetId", shareTargetId);
        payload.put("shareRightType", shareRightType);
        payload.put("sharerId", sharerId);
        return payload;
    }

    @Override
    public void createFromPayload(Map<String, Object> payload) {
        assetId = (String) payload.get("assetId");
        assetName = (String) payload.get("assetName");
        assetTypeDisplayName = (String) payload.get("assetTypeDisplayName");
        rawAssetType = (String) payload.get("rawAssetType");
        shareTargetType = (String) payload.get("shareTargetType");
        shareTargetId = (String) payload.get("shareTargetId");
        shareRightType = (String) payload.get("shareRightType");
        sharerId = (String) payload.get("sharerId");
    }

    @Override
    public void validate() throws EventInvalidFieldException {
        if(
            assetId == null ||
            assetName == null ||
            assetTypeDisplayName == null ||
            rawAssetType == null ||
            shareTargetType == null ||
            shareTargetId == null ||
            shareRightType == null ||
            sharerId == null
        ) {
            throw new EventInvalidFieldException("AssetShared event is missing a required field.");
        }
    }
}
