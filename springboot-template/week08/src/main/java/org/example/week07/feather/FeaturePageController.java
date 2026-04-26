package org.example.week07.feather;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feature")
public class FeaturePageController {

    @GetMapping("/page")
    public String page() {
        return """
                <!DOCTYPE html>
                <html lang="zh-CN">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>热门文章排行榜</title>
                    <style>
                        * { margin: 0; padding: 0; box-sizing: border-box; }
                        body { 
                            font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif; 
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            min-height: 100vh;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            padding: 20px;
                        }
                        .box { 
                            max-width: 700px; 
                            width: 100%;
                            background: linear-gradient(145deg, #ffffff, #f5f0ff);
                            padding: 32px 28px;
                            border-radius: 28px;
                            box-shadow: 0 20px 35px -10px rgba(0,0,0,0.3), inset 0 1px 0 rgba(255,255,255,0.5);
                            text-align: center;
                            border: 1px solid rgba(255,255,255,0.3);
                        }
                        h2 { 
                            margin-top: 0; 
                            margin-bottom: 24px;
                            color: #4a2b7a; 
                            font-size: 28px;
                            letter-spacing: 1px;
                            font-weight: 700;
                            text-shadow: 2px 2px 4px rgba(0,0,0,0.05);
                        }
                        .nav-btn { 
                            display: inline-block; 
                            width: auto;
                            min-width: 240px;
                            margin: 12px 0; 
                            padding: 14px 28px; 
                            border: none;
                            border-radius: 60px; 
                            background: linear-gradient(95deg, #7c3aed, #a855f7);
                            color: white; 
                            text-decoration: none;
                            font-weight: 600;
                            font-size: 18px;
                            transition: all 0.25s ease;
                            box-shadow: 0 6px 14px rgba(124,58,237,0.35);
                            cursor: pointer;
                        }
                        .nav-btn:hover { 
                            background: linear-gradient(95deg, #6d28d9, #9333ea);
                            transform: translateY(-3px);
                            box-shadow: 0 12px 20px rgba(124,58,237,0.4);
                        }
                    </style>
                </head>
                <body>
                <div class="box">
                    <h2>热门文章排行榜</h2>
                    <a class="nav-btn" href="/hot/page">查看热门文章排行榜</a>
                </div>
                </body>
                </html>
                """;
    }
}