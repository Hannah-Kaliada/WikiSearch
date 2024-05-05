import React from 'react';

const TermsOfUsePage = () => {
    return (
        <>
            <header>
                <div className="header-content">
                    <h1><span style={{color: '#fff'}}>Terms of Use</span> - WikiService</h1>
                </div>
            </header>
            <main>
                <section>
                    <h2>1. Service Description</h2>
                    <p>WikiService is a service designed to provide users with a convenient way to search for articles
                        on Wikipedia. It is provided on an "as is" basis.</p>
                </section>
                <section>
                    <h2>2. Limitation of Liability</h2>
                    <p>We are not responsible for the accuracy or timeliness of the information provided by Wikipedia.
                        By using our service, you agree that we are not liable for the content of the articles or any
                        consequences arising from the use of our service.</p>
                </section>
                <section>
                    <h2>3. Use of Information</h2>
                    <p>Information obtained through our service should only be used in accordance with Wikipedia's rules
                        and policies. Please respect copyright and other legal restrictions when using information.</p>
                </section>
                <section>
                    <h2>4. Contact</h2>
                    <p>If you have any questions or suggestions regarding the terms of use, please contact us via
                        email: <a href="mailto:koleda.ann@gmail.com">koleda.ann@gmail.com</a> or Telegram: <a
                            href="https://t.me/jewishmommy">jewishmommy</a></p>
                </section>
            </main>
            <footer>
                <p>&copy; 2024 WikiService. All rights reserved.</p>
            </footer>
            <style>{`
                body {
                    font-family: Arial, sans-serif;
                    line-height: 1.6;
                    margin: 0;
                    padding: 0;
                }
                header {
                    background-color: #333;
                    color: #fff;
                    padding: 20px;
                    text-align: center;
                }
                header h1 {
                    color: #fff;
                }
                main {
                    padding: 20px;
                }
                section {
                    margin-bottom: 30px;
                }
                h1, h2 {
                    color: #333;
                }
                footer {
                    background-color: #333;
                    color: #fff;
                    padding: 10px 20px;
                    text-align: center;
                    position: fixed;
                    bottom: 0;
                    width: 100%;
                }
            `}</style>
        </>
    );
};

export default TermsOfUsePage;
