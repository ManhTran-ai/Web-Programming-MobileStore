/* Lightweight carousel module: autoplay, pause-on-hover, keyboard, dots, captions */
(function () {
    'use strict';

    function Carousel(containerSelector, options) {
        this.container = document.querySelector(containerSelector);
        if (!this.container) return;
        this.track = this.container.querySelector('.carousel-slides') || document.getElementById('carouselSlides');
        this.slides = this.track ? Array.from(this.track.querySelectorAll('.carousel-slide')) : [];
        this.dotsContainer = this.container.querySelector('.carousel-dots') || document.getElementById('carouselDots');
        this.prevBtn = this.container.querySelector('.carousel-nav.prev');
        this.nextBtn = this.container.querySelector('.carousel-nav.next');
        this.currentIndex = 0;
        this.intervalId = null;
        this.options = Object.assign({ interval: 4000, pauseOnHover: true }, options || {});
    }

    Carousel.prototype.update = function () {
        if (!this.track) return;
        // Use percentage-based translate for responsiveness
        this.track.style.transform = 'translateX(-' + (this.currentIndex * 100) + '%)';
        this.updateDots();
    };

    Carousel.prototype.updateDots = function () {
        if (!this.dotsContainer) return;
        var dots = Array.from(this.dotsContainer.children);
        dots.forEach(function (dot, idx) {
            dot.classList.toggle('active', idx === this.currentIndex);
        }, this);
    };

    Carousel.prototype.goTo = function (index) {
        if (!this.slides.length) return;
        this.currentIndex = (index + this.slides.length) % this.slides.length;
        this.update();
    };

    Carousel.prototype.next = function () {
        this.goTo(this.currentIndex + 1);
    };

    Carousel.prototype.prev = function () {
        this.goTo(this.currentIndex - 1);
    };

    Carousel.prototype.startAutoPlay = function () {
        this.stopAutoPlay();
        var self = this;
        this.intervalId = setInterval(function () {
            self.next();
        }, this.options.interval);
    };

    Carousel.prototype.stopAutoPlay = function () {
        if (this.intervalId) {
            clearInterval(this.intervalId);
            this.intervalId = null;
        }
    };

    Carousel.prototype.buildDots = function () {
        if (!this.dotsContainer || !this.slides.length) return;
        this.dotsContainer.innerHTML = '';
        for (var i = 0; i < this.slides.length; i++) {
            var btn = document.createElement('button');
            btn.className = 'carousel-dot' + (i === 0 ? ' active' : '');
            btn.setAttribute('data-slide', String(i));
            btn.addEventListener('click', function (e) {
                var idx = parseInt(this.getAttribute('data-slide'), 10);
                e.preventDefault();
                e.stopPropagation();
                this.carouselRef.goTo(idx);
            }.bind(btn), false);
            // store reference to carousel instance for handler
            btn.carouselRef = this;
            this.dotsContainer.appendChild(btn);
        }
    };

    Carousel.prototype.bindEvents = function () {
        var self = this;
        if (this.prevBtn) {
            this.prevBtn.addEventListener('click', function (e) { e.preventDefault(); self.prev(); });
        }
        if (this.nextBtn) {
            this.nextBtn.addEventListener('click', function (e) { e.preventDefault(); self.next(); });
        }
        if (this.options.pauseOnHover && this.container) {
            this.container.addEventListener('mouseenter', function () { self.stopAutoPlay(); });
            this.container.addEventListener('mouseleave', function () { self.startAutoPlay(); });
        }
        // keyboard navigation
        document.addEventListener('keydown', function (e) {
            if (e.key === 'ArrowLeft') self.prev();
            if (e.key === 'ArrowRight') self.next();
        });
        // handle resize to re-apply transform cleanly
        window.addEventListener('resize', function () { self.update(); });
    };

    Carousel.prototype.init = function () {
        if (!this.track || !this.slides.length) return;
        this.buildDots();
        this.update();
        this.bindEvents();
        if (this.options.interval && this.slides.length > 1) {
            this.startAutoPlay();
        }
    };

    // Auto-init for the hero carousel container
    document.addEventListener('DOMContentLoaded', function () {
        var hero = document.querySelector('.carousel-container');
        if (hero) {
            var c = new Carousel('.carousel-container', { interval: 4000, pauseOnHover: true });
            c.init();
            // expose for debugging if needed
            window.__heroCarousel = c;
        }
    });

})();


