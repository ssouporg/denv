package org.ssoup.denv.server.service.versioning;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: ALB
 * Date: 28/06/2015 14:44
 */
public class SemanticVersion implements Comparable<SemanticVersion> {

    private static final String FORMAT1 = "(\\d)\\.(\\d)\\.(\\d)([A-Za-z][0-9A-Za-z-]*)?";
    private static final Pattern PATTERN1 = Pattern.compile(SemanticVersion.FORMAT1);

    private static final String FORMAT2 = "(\\d)\\.(\\d)([A-Za-z][0-9A-Za-z-]*)?";
    private static final Pattern PATTERN2 = Pattern.compile(SemanticVersion.FORMAT2);

    private Integer major;
    private Integer minor;
    private Integer patch;
    private String special;

    public SemanticVersion(Integer major, Integer minor, Integer patch, String special) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.special = special;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public Integer getPatch() {
        return patch;
    }

    public void setPatch(Integer patch) {
        this.patch = patch;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public static SemanticVersion valueOf(String version) {
        final Matcher matcher = PATTERN1.matcher(version);
        if (matcher.matches()) {
            final int major = Integer.valueOf(matcher.group(1));
            final int minor = Integer.valueOf(matcher.group(2));
            final int patch = Integer.valueOf(matcher.group(3));
            return new SemanticVersion(major, minor, patch, matcher.group(4));
        }
        final Matcher matcher2 = PATTERN2.matcher(version);
        if (matcher2.matches()) {
            final int major = Integer.valueOf(matcher.group(1));
            final int minor = Integer.valueOf(matcher.group(2));
            return new SemanticVersion(major, minor, 0, matcher.group(3));
        }
        throw new IllegalArgumentException("<"+version+"> does not match semantic version format");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SemanticVersion that = (SemanticVersion) o;

        if (major != null ? !major.equals(that.major) : that.major != null) return false;
        if (minor != null ? !minor.equals(that.minor) : that.minor != null) return false;
        if (patch != null ? !patch.equals(that.patch) : that.patch != null) return false;
        return !(special != null ? !special.equals(that.special) : that.special != null);

    }

    @Override
    public int hashCode() {
        int result = major != null ? major.hashCode() : 0;
        result = 31 * result + (minor != null ? minor.hashCode() : 0);
        result = 31 * result + (patch != null ? patch.hashCode() : 0);
        result = 31 * result + (special != null ? special.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(SemanticVersion o) {
        if (major != o.major) {
            return major.compareTo(o.getMajor());
        }
        if (minor != o.getMinor()) {
            return minor.compareTo(o.getMinor());
        }
        if (patch != o.getPatch()) {
            return patch.compareTo(o.getPatch());
        }
        return special.compareTo(o.getSpecial());
    }
}
