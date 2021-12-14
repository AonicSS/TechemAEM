package com.techem.core.models;

import java.math.BigInteger;

import javax.annotation.PostConstruct;
import com.day.cq.dam.api.Asset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewshubContactDownloadItem {

    private Logger logger = LoggerFactory.getLogger(NewshubContactDownloadItem.class);

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name = "downloadFileReference")
    private String downloadFileReference;

    @ValueMapValue(name = "fileLabel")
    private String fileLabel;

    private String fileSize;
    private String fileFormat;

    @PostConstruct
    protected void init() {
        try {
            Asset myAsset = resourceResolver.getResource(downloadFileReference).adaptTo(Asset.class);
            
            fileFormat = FilenameUtils.getExtension(myAsset.getName());
            fileSize = FileUtils.byteCountToDisplaySize(new BigInteger(myAsset.getMetadataValue("dam:size")));
        } catch (Error error) {
            logger.error(error.toString());
        }
    }

    public String getFileLabel() {
        return fileLabel;
    }

    public String getDownloadFileReference() {
        return downloadFileReference;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileFormat() {
        return fileFormat;
    }
}