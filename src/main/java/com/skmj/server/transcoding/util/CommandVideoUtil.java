package com.skmj.server.transcoding.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lc
 */
@Slf4j
public class CommandVideoUtil {
    private final static String DURATION = "Duration: (\\d{2}):(\\d{2}):(\\d{2}\\.\\d{2})";
    private final static String TIME = "time=(\\d{2}):(\\d{2}):(\\d{2}\\.\\d{2})";

    /**
     * 视频转m3u8
     * @param ffmpegPath ffmpeg文件路径
     * @param outputDir 输出目录
     * @param inputUrl 视频地址
     * @return 是否转码成功
     */
    public static boolean executeCommandVideo(String ffmpegPath, String outputDir, String inputUrl) {
        try {
            // 获取输入视频的总时长（以秒为单位）
            double totalDuration = getVideoDuration(ffmpegPath, inputUrl);
            log.info("视频总时长：{}", totalDuration);
            long snowflakeNextId = IdUtil.getSnowflakeNextId();
            // 构建FFmpeg命令
            List<String> ffmpegCommand = new ArrayList<>();
            ffmpegCommand.add(ffmpegPath);
            ffmpegCommand.add("-i");
            ffmpegCommand.add(inputUrl);
            ffmpegCommand.add("-c:v");
            ffmpegCommand.add("libx264");
            ffmpegCommand.add("-c:a");
            ffmpegCommand.add("copy");
            ffmpegCommand.add("-hls_time");
            ffmpegCommand.add("15");
            ffmpegCommand.add("-hls_playlist_type");
            ffmpegCommand.add("vod");
            ffmpegCommand.add("-hls_segment_filename");
            ffmpegCommand.add(outputDir+ File.separator + snowflakeNextId + "_%06d.ts");
            ffmpegCommand.add(outputDir + File.separator+snowflakeNextId+"_index.m3u8");
            if (FileUtil.isEmpty(new File(outputDir))){
                FileUtil.mkdir(new File(outputDir));
            }
            log.info("转码命令：{}", ffmpegCommand);
            ProcessBuilder processBuilder = new ProcessBuilder(ffmpegCommand);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            Pattern timePattern = Pattern.compile(TIME);
            while ((line = reader.readLine()) != null) {
                Matcher matcher = timePattern.matcher(line);
                if (matcher.find()) {
                    int hours = Integer.parseInt(matcher.group(1));
                    int minutes = Integer.parseInt(matcher.group(2));
                    double seconds = Double.parseDouble(matcher.group(3));
                    double currentTime = hours * 3600 + minutes * 60 + seconds;
                    double progress = (currentTime / totalDuration) * 100;
                    log.info("转码进度{}%", Double.valueOf(progress).intValue());
                }
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            log.error("视频转码异常", e);
        }
        return false;
    }

    private static double getVideoDuration(String ffmpegPath, String inputFilePath) {
        String ffmpegDurationCommand = String.format(ffmpegPath + " -i %s", inputFilePath);
        double duration = 0;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(ffmpegDurationCommand.split(" "));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            Pattern durationPattern = Pattern.compile(DURATION);
            while ((line = reader.readLine()) != null) {
                Matcher matcher = durationPattern.matcher(line);
                if (matcher.find()) {
                    int hours = Integer.parseInt(matcher.group(1));
                    int minutes = Integer.parseInt(matcher.group(2));
                    double seconds = Double.parseDouble(matcher.group(3));
                    duration = hours * 3600 + minutes * 60 + seconds;
                    break;
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        return duration;
    }
}
