package com.sb.services.common.enums;

public enum SortCriteriaEnum {

    uid("uid"),

    name("name"),

    tags("tags"),

    description("description"),

    objectType("objectType"),

    //Device specific sort fields
//    installedLocationName(DeviceCustomQueryMapEnum.installedlocation.name()+".name"),
//
//    pointedLocationName(DeviceCustomQueryMapEnum.pointedlocation.name()+".name"),

    hardwareId("hardwareId"),

    serialNo("serialNo"),

    vendor("vendor"),

    model("model"),

    firmwareVersion("firmwareVersion"),

    softwareVersion("softwareVersion"),

//    macAddress(DeviceCustomQueryMapEnum.networkInterface.name()+".macaddress"),
//
//    ipAddress(DeviceCustomQueryMapEnum.networkInterface.name()+".ipaddress"),
//
//    deviceAccessUserName(DeviceCustomQueryMapEnum.userName.name()+".username"),
//    vsomServiceActivationState(DeviceCustomQueryMapEnum.vsomService.name()+".serviceActivationState"),

    modelName("modelName"),

    vsmcSoftwareVersion("vsmcsoftwareversion"),

    softwarePackageVersion("softwarePackageVersion"),

    //for template
    systemDefined("systemDefined"),

    //This sort field being part of Device specific custom query, sort column prefix must be same as
    //the one listed in DeviceCustomQueryMapEnum name
//    umsName( DeviceCustomQueryMapEnum.managedBy.name()+".name"),
    templateName("deviceTemplate.name"),
    aggregateState("deviceState.aggregateState"),

    //black list camera / ignoreddevice
    ipAddressBlackList("ipAddress"),


    //device capability
    type("type"),
    upgradeDriverName("upgradeDriverName"),

    eventTime("eventTime"),

    //jobstatusfilter
    startTime("startTime"),
    endTime("endTime"),
    status("status"),
    action("action"),
    jobCreator("username"),
    deviceName("deviceName"),
    serverName("serverName"),
    errorMessage("errorMessage"),

    //Alert Filter
    alertTime("alertTime"),
    acknowledgedUser("lastAckBy"),
    acknowledgedTime("lastAckTime"),
    clearedUser("lastClearedBy"),
    clearedTime("lastClrTime"),
    extendedType("extendedType"),
    alertType("alertType"),
    userAckState("userAckState"),

    //Alert severity Filter
    category("category"),
    currentSeverity("currentSeverity"),
    defaultSeverity("defaultSeverity"),

    //Auditlog
    activityTimeL("activityTimeL"),
    activityType("activityType"),
    //Given HQL Association mapping
    auditedLocationName("targetLocationRef.refName"),
    auditedTargetName("targetRef.refName"),
    auditedTargetType("targetRef.refObjectType"),
    auditedUser("username"),
    auditedUserIP("userip"),

    //user
    firstname("firstname"),
    lastname("lastname"),

    //user comments filter
    createdTime("createdTime"),

    //issue filter
    severity("severity"),

    //report
    createdBy("createdBy"),
    creationTime("creationTime"),
    reportType("reportType"),
    reportStatus("reportStatus"),
    reportFormat("reportFormat"),
    fileSize("fileSize"),

    //DeviceHealthIssue filter
    issueType("issueType"),
    devicename("devicename"),
    componentName("componentName"),
    servername("servername"),
    locationName("locationName"),
    templatename("templatename"),
    regionname("regionname"),
    count("count"),

    //clipInfo filter
    cameraName("camera.name"),
    userName("userRef.refName"),
    clipLocationName("camera@location@name"),
    server("serverRef.refName"),
    expirationTime("expirationTime"),
    clipState("clipState"),

    //site filter
    user("user"),
    location("location.name"),
    useDP("useDP"),
    maxCameraStreams("maxCameraStreams"),

    //proxy camera cache filter
    cameraId("cameraId"),

    //load balance server cache filter
    serverUid("serverRef.refUid"),
    deviceCount("deviceCount"),


    //mapLayer filter
    lastModifiedTime("lastModified"),
    createTime("createdTime"),
    layerType("layerType"),
    layerFileType("layerFileTye"),

    //mapProvider filter
    url("lastModified"),
    mapProviderType("mapProviderType"),
    activated("activated"),

    cameraRefName("cameraRefName"),
    umsRefName("umsRefName"),
    recordingAlternateId("recordingAlternateId"),

    //session filter
    userRefName("userRef.refName"),

    //eventsMetadataType filter
    triggerType("triggerType"),

    version("version"),
    sdkversion("sdkVersion"),

    //alertnotification policy filter
    alertPolicyType("alertPolicyType"),
    alertNotificationPolicyLocationName("location@name"),
    customEventTypeRef("customEventType@name"),
    customEventSubTypeName("customEventSubTypeName"),
    alertsThreshold("alertsThreshold"),
    initialTime("initialTime"),
    waitTime("waitTime"),

    // CameraRecordingStats filter
    cameraDeviceName("cameraRef.refName"),
    estimatedStorage("estimatedStorage"),
    actualStorage("actualStorage"),
    actualStoragePercentage("actualStoragePercentage"),
    videoEventRecording("videoEventRecording"),
    videoRegularRecording("videoRegularRecording"),
    audio("audio"),
    clips("clips"),
    virtualClips("virtualClips"),
    metadata("metadata"),

    partitionName("partitionName"),
    partitionSize("partitionSize"),

    eventType("eventType"),
    eventStartTime("eventStartTime"),
    numberOfEvents("numberOfEvents"),

    recordingUid("recordingUid"),
    recordingName("recordingName"),
    regularStorage("regularStorage"),
    eventStorage("eventStorage"),
    totalStorage("totalStorage"),

    totalEstimatedStorage("totalEstimatedStorage"),

    eventVideoExpirationTime("eventVideoExpirationTime"),
    regularVideoExpirationTime("regularVideoExpirationTime"),
    regularVideoRetentionDays("regularVideoRetentionDays"),
    eventVideoRetentionDays("eventVideoRetentionDays"),

    //ServiceJobFilter

    userRef("userRef"),
    jobEndTimeInMSec("jobEndTimeInMSec"),
    jobStartTimeInMSec("jobStartTimeInMSec"),

    //VSMX Job
    jobStatus("jobStatus"),
    jobType("jobType"),
    triggerTime("triggerTime"),

    totalPhysicalMemory("umsSystemSummary.totalPhysicalMemory"),
    vsomName("vsomName"),

    lastUpdatedInVsmx("lastUpdatedInVsmx"),

    ;






    //Must be Hibernate mapped field for HQLs and DB equivalent field for custom queries(Device specific queries)
    private String sortableColumn;

    SortCriteriaEnum(String sortableColumn) {
        this.sortableColumn = sortableColumn;
    }

    public String getSortableColumn() {
        return sortableColumn;
    }

    public static SortCriteriaEnum fromValue(String v) {
        for (SortCriteriaEnum c: SortCriteriaEnum.values()) {
            if (c.name().equalsIgnoreCase(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
